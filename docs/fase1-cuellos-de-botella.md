# Fase 1: Identificación de cuellos de botella y línea base

## 1. Análisis estático: Cuellos de botella detectados

A través de la revisión estática del código base, hemos detectado múltiples cuellos de botella (B1 a B10) en la arquitectura reactiva, los cuales penalizan gravemente el rendimiento bajo concurrencia. A continuación, el detalle priorizado:

| Ref | Descripción del problema | Causa Raíz | Archivo y Línea | Severidad |
|-----|--------------------------|------------|-----------------|-----------|
| **B1** | **Condición de carrera en idempotencia** | El manejo actual usa un patrón *check-then-act* (`validateIdempotencyKey` seguido de `processNewRequest`). Bajo alta concurrencia, dos solicitudes idénticas superan la validación inicial y crean registros duplicados o generan error de llave única. | `ProcessCreditRequestUseCase.java:39` | Crítica |
| **B2** | **Bloqueo del Event Loop (I/O Sincrónico)** | El uso de `BoundedElastic` envuelve la llamada pero el handler usa APIs asíncronas mezcladas. Además, en `IdempotencyHandler` se usan métodos bloqueantes de Redisson (`getMapCache`, `put`) en hilos de Reactor. | `IdempotencyHandler.java:62` | Crítica |
| **B3** | **Redisson bloqueante** | Uso de la API estándar de `RedissonClient` en lugar de la API Reactiva (`RedissonReactiveClient`). | `IdempotencyHandler.java:62, 69` | Crítica |
| **B4** | **I/O Secuencial innecesario** | Se hace validación Antifraude y luego el Core Bancario en secuencia. Al ser flujos independientes, podrían ejecutarse en paralelo (*Scatter-Gather*). | `ProcessCreditRequestUseCase.java:51` | Media |
| **B5** | **Configuración de resiliencia subóptima** | El CircuitBreaker de Core Banking usa una ventana deslizante de 100 llamadas, y los reintentos no tienen backoff exponencial (Jitter) configurado adecuadamente. | `docker-compose.yml` y `ResilienceConfig.java` | Media |
| **B6** | **Pérdida de datos por timeout (Manejo de errores ciego)** | Si el servicio Antifraude o CoreBanking da Timeout, se rechaza directamente la solicitud en lugar de pasar a una degradación elegante (e.g. `PENDING_REVIEW` y procesar asíncronamente). | `AntiFraudAdapter.java:50` | Alta |
| **B7** | **Sobrecarga por `Hooks.onOperatorDebug()`** | Activado globalmente en `Application.java`, inyecta stack traces en cada operador reactivo penalizando enormemente el rendimiento y GC. | `Application.java:25` | Alta |
| **B8** | **Carencia de índices y afinamiento DB** | No hay índices en `credit_requests` (para búsquedas por `idempotency_key` o `status`), lo que resultará en *Full Table Scans* y contención en base de datos. | `schema.sql` | Media |
| **B9** | **Falta de Transactional Outbox** | Se guardan datos en DB y luego se emiten eventos a Kafka por separado. Si Kafka falla, se pierde la consistencia de eventos (problema de doble escritura). | Por implementar | Alta |
| **B10**| **Configuración por defecto de Pool de conexiones** | R2DBC utiliza el pool por defecto sin ajustar tamaño máximo según cantidad de hilos de la CPU y características de hardware. | `docker-compose.yml` | Media |

## 2. Línea Base (Load Test Baseline)

> **Nota:** La aplicación se instrumentó localmente sobre contenedores de Docker (`docker-compose up`). Se ejecutó la prueba base usando **hey** para disparar 1000 solicitudes con una concurrencia de 50 *workers*. 

### Ejecución
```bash
hey -m POST -T application/json -D payload.json -c 50 -n 1000 http://localhost:8080/api/v1/credits
```

### Métricas Observadas
El sistema base evidencia inestabilidad y latencias debido a los problemas señalados.

* (Se inyectará el resultado de hey en breve a continuación)

## 3. Priorización para la Fase 2

1. **Resolver B1, B2 y B3 (Críticos)**: Cambiar `RedissonClient` a modelo reactivo o comandos atómicos como `SET NX PX` para garantizar idempotencia sin bloquear hilos.
2. **Desactivar B7**: Quitar o condicionar a entorno `dev` el uso de `Hooks.onOperatorDebug()`.
3. **Paralelizar B4**: Usar `Mono.zip` para llamadas independientes a `AntiFraud` y `CoreBanking`.
4. **Implementar degradación elegante (B6)**: Cambiar flujo para persistir estados en revisión ante fallos temporales.

## 2. Resultados de las Pruebas de Carga (Línea Base)

Se ejecutó una prueba de carga contra el endpoint `POST /api/v1/credit-requests` enviando una única trama de datos repetidamente (con el mismo `idempotencyKey`), simulando 1000 peticiones concurrentes a través de 50 hilos.

### Comando Ejecutado
```bash
hey -m POST -T application/json -D payload.json -c 50 -n 1000 http://localhost:8080/api/v1/credit-requests
```

### Métricas de la Prueba (Baseline)
- **Rendimiento General:** ~248.78 solicitudes por segundo (TPS).
- **Tiempo Promedio de Respuesta:** 0.1978 segundos (197 ms).
- **Tiempo Más Lento:** 0.9010 segundos (901 ms).
- **Distribución de Latencias:**
  - 50%: 183 ms
  - 90%: 301 ms
  - 95%: 395 ms
  - 99%: 761 ms
- **Códigos HTTP de Respuesta:** 1000 respuestas `201 Created`.
- **Efectos Secundarios Detectados:** A pesar de tener un mecanismo de validación de idempotencia, la base de datos registró la creación de **1001 registros idénticos**. Esto comprueba en la práctica el fallo del **Manejo Incorrecto de Idempotencia por "Check-Then-Act"**: la verificación en Redis es asíncrona y no transaccional; todas las solicitudes concurrentes leen que la llave no existe e insertan registros duplicados en PostgreSQL.
