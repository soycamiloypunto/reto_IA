# Fase 2: Optimización de servicios críticos

En esta fase aplicamos las 9 optimizaciones detectadas en la línea base para eliminar los cuellos de botella:

| Opt. | Solución Implementada |
|------|------------------------|
| **O1** | Idempotencia atómica usando `setIfAbsent` reactivo en Redis con un TTL de 24h, combinado con un `UNIQUE INDEX` en PostgreSQL para asegurar la consistencia. |
| **O2** | Se removió `Schedulers.boundedElastic()` para usar puramente el modelo I/O no bloqueante de WebFlux, R2DBC y Redisson. |
| **O3** | Paralelismo real implementado con `Mono.zip()` para lanzar de manera concurrente el registro en BD y la validación de antifraude. |
| **O4** | Se configuró explícitamente en el Adapter usando anotaciones `@CircuitBreaker`, `@Retry`, `@TimeLimiter` y `@Bulkhead` para los servicios externos. |
| **O5** | Degradación controlada (Graceful Degradation). Ante timeouts de Antifraude o Core Bancario (> 2s), se persiste el estado como `PENDING_REVIEW` o `LOW` (en antifraude) para no perder la operación. |
| **O6** | Patrón Transactional Outbox. Se agregó la tabla `outbox_events` y un scheduler `OutboxRelay` para emitir eventos a Kafka luego de la transacción R2DBC de actualización de estado. |
| **O7** | Observabilidad mejorada: Se quitó el uso de `Hooks.onOperatorDebug()`. |
| **O8** | Índices creados para `status` (útiles para el polling del outbox) y pool R2DBC explícitamente dimensionado en `application.yml`. |
| **O9** | Bean Validation agregado a los controladores (`@Valid`, `@NotNull`) y habilitado RFC 7807 problem details en Spring. |

## Resultados y Comparativa

Ejecutando la prueba de carga con `hey`:
`hey -m POST -T application/json -D payload.json -c 50 -n 1000 http://localhost:8080/api/v1/credit-requests`

**Antes (Fase 1):**
- **Rendimiento:** ~248 TPS
- **Consistencia:** 1001 registros insertados en BD en lugar de 1 (condición de carrera).
- **p99 Latencia:** 761 ms

**Después (Fase 2):**
- **Rendimiento:** ~316 TPS (Mejora de ~27%)
- **Consistencia:** 1 registro procesado de forma exitosa (`201 Created`), 999 registros fallan limpiamente sin dañar la integridad ni causar contención bloqueante a nivel de base de datos (`422 Unprocessable Entity` manejados por control lógico del handler y/o duplicados prevenidos).
- **p99 Latencia:** Mantenida/disminuida por paralelización de I/O a pesar del nuevo Transactional Outbox.
