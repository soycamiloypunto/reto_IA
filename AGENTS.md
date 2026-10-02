# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Optimización de rendimiento en sistema de alto rendimiento**.

| | |
|---|---|
| Tema | Desarrollo |
| Nivel | advanced-l2 |
| Chapter | Backend |
| Especialidad | Java |
| Stack | Java / Spring Boot 3.5 |
| Patron arquitectonico | hexagonal/clean con enfoque reactivo en servicios críticos |
| Tiempo estimado | 4 semanas |

## Receta del stack

Esqueleto obligatorio:

- `pom.xml en la raiz`
- `clase con @SpringBootApplication`
- `application.yml en src/main/resources`
- `capa de dominio con entidades y puertos`
- `capa de aplicacion con casos de uso`
- `capa de infraestructura con adaptadores y @RestController`

Trampas conocidas:

- TODA `<version>` del pom va con tres segmentos: la del parent (ej. `3.5.6`) y la de cada dependencia que la lleve (ej. Resilience4j `2.2.0`). `3.4` y `2.0` no existen como artefacto y el build muere resolviendo dependencias.
- Las dependencias que el parent POM gestiona van SIN `<version>`: `spring-boot-starter-web`, `-data-jpa`, `-validation`, `-test`, etc.
- Resilience4j publica un artefacto por linea de Spring Boot. Con Spring Boot 3 va `resilience4j-spring-boot3` con version de tres segmentos (ej. `2.2.0`, no `2.0`). `resilience4j-spring-boot2` es de Spring Boot 2 y rompe el arranque.
- Si usas anotaciones de validacion (`@NotNull`, `@Size`, `@Positive`) declara `spring-boot-starter-validation`: el starter web no las trae.
- El `spring-boot-maven-plugin` tiene que estar en `<build><plugins>` o no se empaqueta ejecutable.
- Spring Boot 3 usa `jakarta.*`, nunca `javax.*`.
- Cada archivo empieza con su `package` y con un `import` por cada clase del proyecto que viva en otro paquete. Usar `PaymentService` desde `infrastructure` sin `import com.x.application.PaymentService` no compila.

Dependencias:

- org.springframework.boot:spring-boot-starter-webflux 3.5.6
- org.springframework.boot:spring-boot-starter-data-r2dbc 3.5.6
- io.r2dbc:r2dbc-postgresql 1.0.5.RELEASE
- org.springframework.boot:spring-boot-starter-actuator n/a
- org.springframework.kafka:spring-kafka 3.3.0
- io.projectreactor.netty:reactor-netty 1.1.23
- io.github.resilience4j:resilience4j-spring-boot3 2.2.0
- org.springframework.boot:spring-boot-starter-validation n/a
- org.springframework.boot:spring-boot-starter-test n/a
- org.springframework.boot:spring-boot-starter-aop n/a
- org.testcontainers:postgresql 1.20.1
- org.testcontainers:kafka 1.20.1
- org.springdoc:springdoc-openapi-starter-webflux-ui 2.6.0
- io.projectreactor:reactor-test n/a
- org.redisson:redisson-spring-boot-starter 3.34.0

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `mvn clean compile` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `mvn clean compile` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Identificación de cuellos de botella**: Reporte detallado de los cuellos de botella detectados, incluyendo métricas de rendimiento y posibles causas.
- **Fase 2 — Optimización de servicios críticos**: Servicios optimizados con pruebas de rendimiento que demuestran mejoras significativas.
- **Fase 3 — Validación y escalabilidad**: Reporte de pruebas de carga con resultados y propuestas de mejora para la escalabilidad.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Superficie de practica (NO completes)

Estos archivos SON el ejercicio de la persona. No los implementes; deja stubs. No toques la logica que el reto pide completar.

- [ ] `src/main/java/com/bank/credit/infrastructure/config/ResilienceConfig.java` — El topic pide resiliencia: este archivo es el ejercicio.

## Lo que falta y tenes que completar

### 1. Referencias colgando (92)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `com.bank.credit.domain.events.CreditRequestApprovedEvent`
      El import com.bank.credit.domain.events.CreditRequestApprovedEvent usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `com.bank.credit.domain.events.CreditRequestRejectedEvent`
      El import com.bank.credit.domain.events.CreditRequestRejectedEvent usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- [ ] `src/main/java/com/bank/credit/Application.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Hooks pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/domain/ports/CreditRequestPort.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/domain/ports/AntiFraudPort.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/domain/ports/CoreBankingPort.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/repositories/CreditRequestRepository.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `reactor.core.scheduler`
      El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `io.swagger.v3`
      El import io.swagger.v3.oas.annotations.Operation pertenece a io.swagger.v3, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `reactor.kafka.receiver`
      El import reactor.kafka.receiver.KafkaReceiver pertenece a reactor.kafka.receiver, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `reactor.kafka.sender`
      El import reactor.kafka.sender.KafkaSender pertenece a reactor.kafka.sender, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapterTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `IdempotencyHandler.checkAndStore`
      Se invoca `checkAndStore` sobre `IdempotencyHandler`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CreditRequest.customerId`
      Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CreditRequest.id`
      Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.success`
      Se invoca `success` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.referenceId`
      Se invoca `referenceId` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.message`
      Se invoca `message` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.idempotencyKey`
      Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.customerId`
      Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.requestedAmount`
      Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.idempotencyKey`
      Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.requestedAmount`
      Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.customerId`
      Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.operationNumber`
      Se invoca `operationNumber` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.channel`
      Se invoca `channel` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.requestedAmount`
      Se invoca `requestedAmount` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.customerId`
      Se invoca `customerId` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.termMonths`
      Se invoca `termMonths` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.status`
      Se invoca `status` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findById`
      Se invoca `findById` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findByIdempotencyKey`
      Se invoca `findByIdempotencyKey` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findAll`
      Se invoca `findAll` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.id`
      Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.customerId`
      Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.requestedAmount`
      Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.termMonths`
      Se invoca `termMonths` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.idempotencyKey`
      Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.createdAt`
      Se invoca `createdAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.id`
      Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.operationNumber`
      Se invoca `operationNumber` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.channel`
      Se invoca `channel` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantId`
      Se invoca `applicantId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantName`
      Se invoca `applicantName` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantEmail`
      Se invoca `applicantEmail` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantPhone`
      Se invoca `applicantPhone` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.requestedAmount`
      Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.termMonths`
      Se invoca `termMonths` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.interestRate`
      Se invoca `interestRate` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.creditType`
      Se invoca `creditType` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.status`
      Se invoca `status` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.requestedAt`
      Se invoca `requestedAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.processedAt`
      Se invoca `processedAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.approvedBy`
      Se invoca `approvedBy` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.rejectionReason`
      Se invoca `rejectionReason` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.antiFraudScore`
      Se invoca `antiFraudScore` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.antiFraudDecision`
      Se invoca `antiFraudDecision` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.coreBankingReference`
      Se invoca `coreBankingReference` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/domain/exceptions/AntiFraudValidationException.java` — `FraudRiskLevel.name`
      Se invoca `name` sobre `FraudRiskLevel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getMessage`
      Se invoca `getMessage` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getIdempotencyKey`
      Se invoca `getIdempotencyKey` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getChannel`
      Se invoca `getChannel` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getOriginalRequestTime`
      Se invoca `getOriginalRequestTime` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRemainingSeconds`
      Se invoca `getRemainingSeconds` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.hasErrors`
      Se invoca `hasErrors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getValidationErrors`
      Se invoca `getValidationErrors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getErrorCount`
      Se invoca `getErrorCount` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRiskLevel`
      Se invoca `getRiskLevel` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRiskFactors`
      Se invoca `getRiskFactors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getValidationTime`
      Se invoca `getValidationTime` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.hasRiskFactors`
      Se invoca `hasRiskFactors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.isHighRisk`
      Se invoca `isHighRisk` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `CoreBankingPort.submitApplication`
      Se invoca `submitApplication` sobre `CoreBankingPort`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `CreditRequest.id`
      Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java` — `ProcessCreditRequestUseCase.getById`
      Se invoca `getById` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `pom.xml` — `io.r2dbc:r2dbc-postgresql@1.0.5.RELEASE`
      io.r2dbc:r2dbc-postgresql declara la version 1.0.5.RELEASE, pero Maven Central respondio que esa version no existe. Es una version inventada: reemplazala por una version publicada real, o si no se conoce con certeza, usa el mecanismo centralizado del ecosistema (BOM/parent/platform/version catalog) y no declares una version individual.

### Presentes (26)

- `pom.xml`
- `src/main/java/com/bank/credit/Application.java`
- `src/main/resources/application.yml`
- `src/main/java/com/bank/credit/domain/models/CreditRequest.java`
- `src/main/java/com/bank/credit/domain/ports/CreditRequestPort.java`
- `src/main/java/com/bank/credit/domain/ports/AntiFraudPort.java`
- `src/main/java/com/bank/credit/domain/ports/CoreBankingPort.java`
- `src/main/java/com/bank/credit/infrastructure/repositories/CreditRequestRepository.java`
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java`
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java`
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java`
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java`
- `src/main/java/com/bank/credit/infrastructure/config/ResilienceConfig.java`
- `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java`
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java`
- `src/main/java/com/bank/credit/domain/exceptions/CreditRequestNotFoundException.java`
- `src/main/java/com/bank/credit/domain/exceptions/IdempotencyConflictException.java`
- `src/main/java/com/bank/credit/domain/exceptions/InvalidCreditRequestException.java`
- `src/main/java/com/bank/credit/domain/exceptions/AntiFraudValidationException.java`
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java`
- `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java`
- `src/test/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapterTest.java`
- `src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java`
- `Dockerfile`
- `docker-compose.yml`
- `README.md`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/bank/credit`
- `src/main/java/com/bank/credit/application`
- `src/main/java/com/bank/credit/domain`
- `src/main/java/com/bank/credit/infrastructure`
- `src/main/java/com/bank/credit/infrastructure/adapters`
- `src/main/java/com/bank/credit/infrastructure/config`
- `src/main/java/com/bank/credit/infrastructure/controllers`
- `src/main/java/com/bank/credit/infrastructure/repositories`
- `src/main/resources`
- `src/test/java/com/bank/credit`

## Verificacion

```bash
mvn clean compile
```

El comando tiene que pasar SIN implementar los archivos de la superficie de practica: solo andamiaje.

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **hexagonal/clean con enfoque reactivo en servicios críticos**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Backend, Especialidad Desarrollador, Tecnología Java, Advanced
- Brecha que el reto ataca: Ha trabajado con un asistente de IA (Github Copilot / Amazon CodeWhisperer / etc)
- Mision: Candidato con experiencia avanzada en Backend con Java, trabajando en sistemas de alto rendimiento y arquitecturas escalables

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
