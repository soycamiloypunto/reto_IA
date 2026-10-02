# Fase 3: Validación y Escalabilidad

En esta fase final validamos el impacto conjunto de todas las optimizaciones bajo una prueba de carga continua.

## Escenario de Prueba
Se utilizó `hey` con los siguientes parámetros contra el entorno dockerizado local:
- **Concurrencia:** 200 hilos concurrentes
- **Duración:** 10 segundos continuos
- **Comando:** `hey -m POST -T application/json -D payload.json -c 200 -z 10s http://localhost:8080/api/v1/credit-requests`

## Resultados Obtenidos
- **Rendimiento (TPS):** ~658 solicitudes por segundo.
- **Latencias:**
  - **Promedio:** 303 ms
  - **p50:** 233 ms
  - **p90:** 472 ms
  - **p99:** 1.27 s
- **Tasa de Errores Técnicos (5xx):** 0%. El sistema respondió limpiamente.
- **Validación de Integridad:** Se procesaron miles de peticiones con la misma llave de idempotencia; el sistema solo insertó 1 registro inicial y rechazó el resto correctamente con `422 Unprocessable Entity` sin generar bloqueos en la base de datos gracias a Redis y a la delegación controlada del handler.

## Conclusión y Escalabilidad
Las optimizaciones (I/O completamente no bloqueante, paralelismo con `Mono.zip`, resiliencia aislada y Transactional Outbox) demostraron multiplicar el rendimiento base de la Fase 1 (~248 TPS) por un factor de **2.6x** en este hardware específico.

Para alcanzar **10,000 TPS**, se propone:
1. **Infraestructura Dedicada:** Migrar Postgres, Redis y Kafka a servicios administrados (ej. RDS, ElastiCache, MSK) para remover la carga de CPU y memoria de los contenedores Docker locales.
2. **Auto-Scaling:** Escalar horizontalmente las réplicas del `credit-processing-system` en un cluster de Kubernetes con balanceador de carga.
3. **Optimización de Pooling:** Aumentar `max-size` de R2DBC proporcional a los núcleos disponibles en producción.

## Pasos para Certificar la Solución (Instrucciones para el Evaluador)

Puedes replicar estas pruebas en tu propia terminal para certificar la escalabilidad y consistencia logradas.

### 1. Preparar el entorno
Levanta la infraestructura completa en Docker:
```bash
docker-compose down -v
docker-compose up -d
```
Verifica que la aplicación reporta un estado saludable:
```bash
curl -s http://localhost:8080/actuator/health
```
*(Deberías recibir `{"status":"UP"}`)*

### 2. Prueba funcional básica (1 petición)
Envía un POST normal para verificar el flujo de aprobación:
```bash
curl -s -X POST http://localhost:8080/api/v1/credit-requests \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CLIENT-001",
    "requestedAmount": 50000.00,
    "termMonths": 24,
    "interestRate": 12.5,
    "channel": "WEB",
    "operationNumber": "OP-TEST-01"
  }'
```

### 3. Prueba de carga y consistencia (Idempotencia Atómica)
Si tienes instalado `hey` (puedes instalarlo con `brew install hey`), ejecuta esta prueba agresiva. Esto enviará cientos de peticiones con la **misma** clave de idempotencia (`OP-TEST-02`) para validar que solo se inserte en base de datos la primera.

```bash
# Crear un archivo de payload temporal
echo '{ "customerId": "CLIENT-002", "requestedAmount": 15000.00, "termMonths": 12, "channel": "MOBILE", "operationNumber": "OP-TEST-02" }' > test-payload.json

# Ejecutar carga de estrés (200 concurrentes, 10 segundos)
hey -m POST -T application/json -D test-payload.json -c 200 -z 10s http://localhost:8080/api/v1/credit-requests
```
*(Verás en el reporte de `hey` múltiples respuestas `422/409` como rechazo rápido desde Redis/R2DBC, y solo una respuesta exitosa, con 0% de errores internos `500`)*

### 4. Validar el estado en Base de Datos
Comprueba que el mecanismo de _Transactional Outbox_ y la Idempotencia ataron correctamente la persistencia:
```bash
docker exec credit-postgres psql -U credit_user -d credit_db -c "SELECT status, idempotency_key FROM credit_requests WHERE idempotency_key = 'MOBILE:OP-TEST-02';"
```
*(Solo debe retornar **un único** registro).*

```bash
docker exec credit-postgres psql -U credit_user -d credit_db -c "SELECT COUNT(*) FROM outbox_events;"
```
*(El OutboxRelay debió procesar y marcar como `PROCESSED` o emitir a Kafka el registro de este cambio).*
