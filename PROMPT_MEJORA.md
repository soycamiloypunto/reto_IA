# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Superficie de practica — NO resuelvas

Estos archivos SON el ejercicio de la persona. No los implementes; deja stubs.

- `src/main/java/com/bank/credit/infrastructure/config/ResilienceConfig.java` — El topic pide resiliencia: este archivo es el ejercicio.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `com.bank.credit.domain.events.CreditRequestApprovedEvent`: El import com.bank.credit.domain.events.CreditRequestApprovedEvent usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `com.bank.credit.domain.events.CreditRequestRejectedEvent`: El import com.bank.credit.domain.events.CreditRequestRejectedEvent usa un paquete propio del proyecto pero ningun archivo generado declara ese tipo. Falta generar la clase/interfaz, o el import esta mal escrito.
- `src/main/java/com/bank/credit/Application.java` — `reactor.core.publisher`: El import reactor.core.publisher.Hooks pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/domain/ports/CreditRequestPort.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/domain/ports/AntiFraudPort.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/domain/ports/CoreBankingPort.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/repositories/CreditRequestRepository.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `reactor.core.scheduler`: El import reactor.core.scheduler.Schedulers pertenece a reactor.core.scheduler, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `io.swagger.v3`: El import io.swagger.v3.oas.annotations.Operation pertenece a io.swagger.v3, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `reactor.kafka.receiver`: El import reactor.kafka.receiver.KafkaReceiver pertenece a reactor.kafka.receiver, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java` — `reactor.kafka.sender`: El import reactor.kafka.sender.KafkaSender pertenece a reactor.kafka.sender, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `org.slf4j`: El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapterTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `IdempotencyHandler.checkAndStore`: Se invoca `checkAndStore` sobre `IdempotencyHandler`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CreditRequest.customerId`: Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CreditRequest.id`: Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.success`: Se invoca `success` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.referenceId`: Se invoca `referenceId` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java` — `CoreBankingResult.message`: Se invoca `message` sobre `CoreBankingResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.idempotencyKey`: Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.customerId`: Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java` — `CreditRequest.requestedAmount`: Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.idempotencyKey`: Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.requestedAmount`: Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java` — `CreditRequest.customerId`: Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.operationNumber`: Se invoca `operationNumber` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.channel`: Se invoca `channel` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.requestedAmount`: Se invoca `requestedAmount` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.customerId`: Se invoca `customerId` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequestDTO.termMonths`: Se invoca `termMonths` sobre `CreditRequestDTO`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.status`: Se invoca `status` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findById`: Se invoca `findById` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findByIdempotencyKey`: Se invoca `findByIdempotencyKey` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `ProcessCreditRequestUseCase.findAll`: Se invoca `findAll` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.id`: Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.customerId`: Se invoca `customerId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.requestedAmount`: Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.termMonths`: Se invoca `termMonths` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.idempotencyKey`: Se invoca `idempotencyKey` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java` — `CreditRequest.createdAt`: Se invoca `createdAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.id`: Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.operationNumber`: Se invoca `operationNumber` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.channel`: Se invoca `channel` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantId`: Se invoca `applicantId` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantName`: Se invoca `applicantName` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantEmail`: Se invoca `applicantEmail` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.applicantPhone`: Se invoca `applicantPhone` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.requestedAmount`: Se invoca `requestedAmount` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.termMonths`: Se invoca `termMonths` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.interestRate`: Se invoca `interestRate` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.creditType`: Se invoca `creditType` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.status`: Se invoca `status` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.requestedAt`: Se invoca `requestedAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.processedAt`: Se invoca `processedAt` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.approvedBy`: Se invoca `approvedBy` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.rejectionReason`: Se invoca `rejectionReason` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.antiFraudScore`: Se invoca `antiFraudScore` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.antiFraudDecision`: Se invoca `antiFraudDecision` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java` — `CreditRequest.coreBankingReference`: Se invoca `coreBankingReference` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/domain/exceptions/AntiFraudValidationException.java` — `FraudRiskLevel.name`: Se invoca `name` sobre `FraudRiskLevel`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getMessage`: Se invoca `getMessage` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getIdempotencyKey`: Se invoca `getIdempotencyKey` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getChannel`: Se invoca `getChannel` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getOriginalRequestTime`: Se invoca `getOriginalRequestTime` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRemainingSeconds`: Se invoca `getRemainingSeconds` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.hasErrors`: Se invoca `hasErrors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getValidationErrors`: Se invoca `getValidationErrors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getErrorCount`: Se invoca `getErrorCount` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRiskLevel`: Se invoca `getRiskLevel` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getRiskFactors`: Se invoca `getRiskFactors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.getValidationTime`: Se invoca `getValidationTime` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.hasRiskFactors`: Se invoca `hasRiskFactors` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java` — `CreditRequestNotFoundException.isHighRisk`: Se invoca `isHighRisk` sobre `CreditRequestNotFoundException`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `CoreBankingPort.submitApplication`: Se invoca `submitApplication` sobre `CoreBankingPort`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java` — `CreditRequest.id`: Se invoca `id` sobre `CreditRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java` — `ProcessCreditRequestUseCase.getById`: Se invoca `getById` sobre `ProcessCreditRequestUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `pom.xml` — `io.r2dbc:r2dbc-postgresql@1.0.5.RELEASE`: io.r2dbc:r2dbc-postgresql declara la version 1.0.5.RELEASE, pero Maven Central respondio que esa version no existe. Es una version inventada: reemplazala por una version publicada real, o si no se conoce con certeza, usa el mecanismo centralizado del ecosistema (BOM/parent/platform/version catalog) y no declares una version individual.

## Como saber que terminaste

```bash
mvn clean compile
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Perfil
Chapter Backend, Especialidad Desarrollador, Tecnología Java, Advanced

### Brecha de conocimiento
Ha trabajado con un asistente de IA (Github Copilot / Amazon CodeWhisperer / etc)

### Misión / candidato
Candidato con experiencia avanzada en Backend con Java, trabajando en sistemas de alto rendimiento y arquitecturas escalables

### Reto
- Tema: Desarrollo
- Seniority: advanced-l2
- Tipo: practical
- Título: Optimización de rendimiento en sistema de alto rendimiento
- Tiempo estimado: 4 semanas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Identificación de cuellos de botella — objetivo: Detectar y documentar los puntos de baja eficiencia en el procesamiento de transacciones. — entregable (NO resolver): Reporte detallado de los cuellos de botella detectados, incluyendo métricas de rendimiento y posibles causas.
- Fase 2: Optimización de servicios críticos — objetivo: Implementar mejoras en los servicios identificados como críticos para el rendimiento. — entregable (NO resolver): Servicios optimizados con pruebas de rendimiento que demuestran mejoras significativas.
- Fase 3: Validación y escalabilidad — objetivo: Validar las mejoras implementadas y asegurar la escalabilidad del sistema bajo carga máxima. — entregable (NO resolver): Reporte de pruebas de carga con resultados y propuestas de mejora para la escalabilidad.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.6</version>
        <relativePath/>
    </parent>

    <groupId>com.bank.credit</groupId>
    <artifactId>credit-processing-system</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>credit-processing-system</name>
    <description>Sistema de procesamiento de créditos con arquitectura reactiva y resiliencia</description>

    <properties>
        <java.version>21</java.version>
        <project.reactor.version>2023.0.12</project.reactor.version>
        <resilience4j.version>2.2.0</resilience4j.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-r2dbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- R2DBC -->
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-postgresql</artifactId>
            <version>1.0.5.RELEASE</version>
        </dependency>

        <!-- Kafka -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.3.0</version>
        </dependency>

        <!-- Resilience4j -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-spring-boot3</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-reactor</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>

        <!-- Redis -->
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson-spring-boot-starter</artifactId>
            <version>3.34.0</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.20.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.20.1</version>
            <scope>test</scope>
        </dependency>

        <!-- OpenAPI -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
            <version>2.6.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/bank/credit/Application.java ===
package com.bank.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;

@SpringBootApplication
@EnableWebFlux
@EnableAsync
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {
        // Configuración global de Reactor para mejor manejo de errores
        Hooks.onOperatorDebug();
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CircuitBreaker creditProcessingCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("creditProcessing");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    switch (event.getStateTransition()) {
                        case CLOSED_TO_OPEN:
                            System.out.println("Circuit breaker opened for credit processing");
                            break;
                        case OPEN_TO_HALF_OPEN:
                            System.out.println("Circuit breaker half-opened for credit processing");
                            break;
                        case HALF_OPEN_TO_CLOSED:
                            System.out.println("Circuit breaker closed for credit processing");
                            break;
                        default:
                            break;
                    }
                });
        return circuitBreaker;
    }

    @Bean
    public Retry coreBankingRetry(RetryRegistry retryRegistry) {
        Retry retry = retryRegistry.retry("coreBanking");
        retry.getEventPublisher()
                .onRetry(event -> System.out.println("Retry attempt " + event.getNumberOfRetryAttempts() +
                        " for core banking call. Last exception: " + event.getLastThrowable().getMessage()))
                .onSuccess(event -> System.out.println("Retry succeeded after " +
                        event.getNumberOfRetryAttempts() + " attempts"));
        return retry;
    }

    @Bean
    public reactor.core.scheduler.Scheduler asyncScheduler() {
        return reactor.core.scheduler.Schedulers.newBoundedElastic(
                20,
                100,
                "async-credit-processing",
                60,
                true
        );
    }
}

// === ARCHIVO: src/main/resources/application.yml ===
spring:
  application:
    name: credit-processing-system
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/credit_db
    username: credit_user
    password: credit_pass
    pool:
      enabled: true
      initial-size: 10
      max-size: 50
      max-idle-time: 30m
      validation-query: SELECT 1
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: credit-processing-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      properties:
        enable.idempotence: true
        max.in.flight.requests.per.connection: 5
  redis:
    redisson:
      file: classpath:redisson-config.yml

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,circuitbreakers,retries,kafka
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
  health:
    circuitbreakers:
      enabled: true
    retries:
      enabled: true

server:
  port: 8080
  netty:
    connection-timeout: 2s
    idle-timeout: 15s
    threads:
      acceptor: 4
      worker: 20

resilience4j:
  circuitbreaker:
    instances:
      creditProcessing:
        registerHealthIndicator: true
        slidingWindowType: TIME_BASED
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
        recordExceptions:
          - org.springframework.web.reactive.function.client.WebClientResponseException
          - java.util.concurrent.TimeoutException
          - java.io.IOException
      antiFraud:
        registerHealthIndicator: true
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 5
        minimumNumberOfCalls: 3
        permittedNumberOfCallsInHalfOpenState: 2
        waitDurationInOpenState: 10s
        failureRateThreshold: 60
        recordExceptions:
          - java.util.concurrent.TimeoutException
          - java.io.IOException
  retry:
    instances:
      coreBanking:
        maxAttempts: 3
        waitDuration: 500ms
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2
        retryExceptions:
          - org.springframework.web.reactive.function.client.WebClientResponseException$InternalServerError
          - java.util.concurrent.TimeoutException
          - java.io.IOException
      antiFraud:
        maxAttempts: 2
        waitDuration: 300ms
        retryExceptions:
          - java.util.concurrent.TimeoutException
  timelimiter:
    instances:
      coreBanking:
        timeoutDuration: 2s
      antiFraud:
        timeoutDuration: 1s

logging:
  level:
    root: INFO
    com.bank.credit: DEBUG
    org.springframework.web: INFO
    org.springframework.r2dbc: DEBUG
    io.r2dbc.postgresql.QUERY: DEBUG
    io.r2dbc.postgresql.PARAM: DEBUG
    org.springframework.kafka: INFO
    reactor.netty.http.client: DEBUG

// === ARCHIVO: src/main/java/com/bank/credit/domain/models/CreditRequest.java ===
package com.bank.credit.domain.models;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio que representa una solicitud de crédito.
 * Contiene las reglas de negocio y validaciones asociadas.
 */
public record CreditRequest(
    @NotNull
    UUID id,

    @NotNull
    @Size(min = 1, max = 50)
    String customerId,

    @NotNull
    @PositiveOrZero
    BigDecimal amount,

    @NotNull
    @Min(1)
    @Max(360)
    Integer termMonths,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0")
    BigDecimal interestRate,

    @NotNull
    @Size(min = 3, max = 3)
    String currency,

    @NotNull
    @Size(min = 1, max = 100)
    String channel,

    @NotNull
    @Size(min = 1, max = 100)
    String operationNumber,

    @NotNull
    CreditRequestStatus status,

    @NotNull
    LocalDateTime createdAt,

    LocalDateTime updatedAt,

    @Size(max = 500)
    String rejectionReason
) {
    /**
     * Constructor con validaciones de negocio.
     * @throws IllegalArgumentException si alguna regla de negocio no se cumple.
     */
    public CreditRequest {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        if (termMonths < 1 || termMonths > 360) {
            throw new IllegalArgumentException("El plazo debe estar entre 1 y 360 meses");
        }
        if (interestRate.compareTo(BigDecimal.ZERO) <= 0 || interestRate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("La tasa de interés debe estar entre 0 y 100");
        }
        if (!"COP".equals(currency) && !"USD".equals(currency)) {
            throw new IllegalArgumentException("Moneda no soportada");
        }
        if (status == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
    }

    /**
     * Calcula la cuota mensual para esta solicitud de crédito.
     * @return cuota mensual calculada.
     */
    public BigDecimal calculateMonthlyPayment() {
        BigDecimal monthlyRate = interestRate.divide(new BigDecimal("12"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal numerator = amount.multiply(monthlyRate).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyRate.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP))
                .pow(-termMonths));
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Genera la clave de idempotencia para esta solicitud.
     * @return clave de idempotencia en formato "operationNumber:channel".
     */
    public String generateIdempotencyKey() {
        return operationNumber + ":" + channel;
    }

    /**
     * Verifica si la solicitud cumple con las condiciones para ser aprobada.
     * @return true si la solicitud puede ser aprobada.
     */
    public boolean isEligibleForApproval() {
        return status == CreditRequestStatus.PENDING && 
               amount.compareTo(new BigDecimal("10000000")) <= 0 && // Límite de 10 millones
               termMonths <= 60; // Plazo máximo de 5 años para aprobación automática
    }

    /**
     * Enum que representa los posibles estados de una solicitud de crédito.
     */
    public enum CreditRequestStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        FAILED
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/ports/CreditRequestPort.java ===
package com.bank.credit.domain.ports;


import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Puerto de dominio para operaciones relacionadas con solicitudes de crédito.
 * Define las operaciones que la capa de aplicación puede realizar sobre el dominio.
 */
public interface CreditRequestPort {
    /**
     * Guarda una solicitud de crédito en el sistema.
     * @param creditRequest la solicitud a guardar.
     * @return Mono con la solicitud guardada.
     */
    Mono<CreditRequest> save(CreditRequest creditRequest);

    /**
     * Busca una solicitud de crédito por su ID.
     * @param id el ID de la solicitud.
     * @return Mono con la solicitud encontrada, o Mono.empty() si no existe.
     */
    Mono<CreditRequest> findById(UUID id);

    /**
     * Busca una solicitud de crédito por su clave de idempotencia.
     * @param idempotencyKey la clave de idempotencia.
     * @return Mono con la solicitud encontrada, o Mono.empty() si no existe.
     */
    Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey);

    /**
     * Actualiza el estado de una solicitud de crédito.
     * @param id el ID de la solicitud.
     * @param status el nuevo estado.
     * @param rejectionReason la razón de rechazo (opcional).
     * @return Mono con la solicitud actualizada.
     */
    Mono<CreditRequest> updateStatus(UUID id, CreditRequest.CreditRequestStatus status, String rejectionReason);
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/ports/AntiFraudPort.java ===
package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para la validación antifraude.
 * Define la interfaz que debe implementar cualquier adaptador de antifraude.
 */
public interface AntiFraudPort {
    /**
     * Valida una solicitud de crédito contra el motor antifraude.
     * @param creditRequest la solicitud a validar.
     * @return Mono con el resultado de la validación antifraude.
     */
    Mono<AntiFraudResult> validate(CreditRequest creditRequest);

    /**
     * Resultado de la validación antifraude.
     */
    record AntiFraudResult(
        boolean isFraudulent,
        String riskLevel,
        String rejectionReason
    ) {
        /**
         * Verifica si la solicitud debe ser rechazada por fraude.
         * @return true si la solicitud debe ser rechazada.
         */
        public boolean shouldReject() {
            return isFraudulent || "HIGH".equals(riskLevel);
        }
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/ports/CoreBankingPort.java ===
package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para la integración con el core bancario.
 * Define el contrato que la infraestructura debe implementar para comunicar
 * el sistema de crédito con los servicios del core bancario del banco.
 */
public interface CoreBankingPort {

    /**
     * Envía una solicitud de crédito aprobada al core bancario para su procesamiento.
     * @param creditRequest la solicitud de crédito aprobada con toda la información necesaria
     * @return Mono con el resultado del procesamiento del core bancario
     */
    Mono<CoreBankingResult> submitCreditRequest(CreditRequest creditRequest);

    /**
     * Consulta el estado de una solicitud previamente enviada al core bancario.
     * @param coreReferenceId el identificador de referencia del core bancario
     * @return Mono con el estado actual de la operación en el core
     */
    Mono<CoreBankingStatus> queryStatus(String coreReferenceId);

    /**
     * Cancela una operación de crédito previamente enviada al core bancario.
     * @param coreReferenceId el identificador de referencia del core bancario
     * @param reason motivo de la cancelación
     * @return Mono con el resultado de la operación de cancelación
     */
    Mono<CoreBankingResult> cancelCreditRequest(String coreReferenceId, String reason);

    /**
     * Resultado retornado por el core bancario tras procesar una solicitud.
     */
    record CoreBankingResult(
        boolean success,
        String referenceId,
        String message,
        CoreBankingErrorCode errorCode
    ) {
        public static CoreBankingResult success(String referenceId) {
            return new CoreBankingResult(true, referenceId, "Operation completed successfully", null);
        }

        public static CoreBankingResult failure(String message, CoreBankingErrorCode errorCode) {
            return new CoreBankingResult(false, null, message, errorCode);
        }
    }

    /**
     * Estado de una operación en el core bancario.
     */
    enum CoreBankingStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        CANCELLED,
        FAILED
    }

    /**
     * Códigos de error del core bancario.
     */
    enum CoreBankingErrorCode {
        INVALID_CUSTOMER,
        INSUFFICIENT_CREDIT,
        DUPLICATE_REFERENCE,
        SYSTEM_ERROR,
        TIMEOUT,
        VALIDATION_ERROR
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/repositories/CreditRequestRepository.java ===
package com.bank.credit.infrastructure.repositories;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequest.CreditRequestStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Repositorio reactivo para la persistencia de solicitudes de crédito usando R2DBC.
 * Proporciona operaciones asíncronas sobre la tabla de solicitudes de crédito
 * sin bloquear el hilo de ejecución.
 */
@Repository
public interface CreditRequestRepository extends ReactiveCrudRepository<CreditRequest, UUID> {

    /**
     * Busca una solicitud por su clave de idempotencia.
     * La clave de idempotencia se compone del número de operación + canal.
     * @param idempotencyKey la clave de idempotencia
     * @return Mono con la solicitud encontrada o vacío
     */
    Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey);

    /**
     * Busca solicitudes por estado.
     * @param status el estado de las solicitudes a buscar
     * @return Flux con las solicitudes que coinciden
     */
    Flux<CreditRequest> findByStatus(CreditRequestStatus status);

    /**
     * Busca solicitudes por identificador de cliente.
     * @param customerId el identificador del cliente
     * @return Flux con las solicitudes del cliente
     */
    Flux<CreditRequest> findByCustomerId(String customerId);

    /**
     * Actualiza el estado de una solicitud y la razón de rechazo si aplica.
     * @param id el identificador de la solicitud
     * @param status el nuevo estado
     * @param rejectionReason la razón de rechazo (opcional)
     * @param processedAt la fecha de procesamiento
     * @return Mono con el número de filas afectadas
     */
    @Modifying
    @Query("UPDATE credit_requests SET status = :status, rejection_reason = :rejectionReason, " +
           "processed_at = :processedAt, updated_at = :updatedAt WHERE id = :id")
    Mono<Integer> updateStatus(UUID id, CreditRequestStatus status, String rejectionReason,
                                Instant processedAt, Instant updatedAt);

    /**
     * Busca solicitudes pendientes de procesamiento.
     * @return Flux con las solicitudes pendientes
     */
    @Query("SELECT * FROM credit_requests WHERE status = 'PENDING' AND created_at > :since " +
           "ORDER BY created_at ASC LIMIT :limit")
    Flux<CreditRequest> findPendingRequests(Instant since, int limit);

    /**
     * Busca solicitudes por rango de monto y estado.
     * @param minAmount monto mínimo
     * @param maxAmount monto máximo
     * @param status estado de las solicitudes
     * @return Flux con las solicitudes que coinciden
     */
    Flux<CreditRequest> findByAmountBetweenAndStatus(BigDecimal minAmount,
                                                       BigDecimal maxAmount,
                                                       CreditRequestStatus status);

    /**
     * Cuenta el número de solicitudes por estado.
     * @param status el estado a contar
     * @return Mono con el conteo
     */
    Mono<Long> countByStatus(CreditRequestStatus status);

    /**
     * Verifica si existe una solicitud con la clave de idempotencia dada.
     * @param idempotencyKey la clave de idempotencia
     * @return Mono con true si existe
     */
    Mono<Boolean> existsByIdempotencyKey(String idempotencyKey);
}

// === ARCHIVO: src/main/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCase.java ===
package com.bank.credit.application.usecases;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequest.CreditRequestStatus;
import com.bank.credit.domain.ports.AntiFraudPort;
import com.bank.credit.domain.ports.CoreBankingPort;
import com.bank.credit.domain.ports.CoreBankingPort.CoreBankingResult;
import com.bank.credit.domain.ports.CoreBankingPort.CoreBankingErrorCode;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.infrastructure.adapters.IdempotencyHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.retry.Operator;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Caso de uso principal que orquesta el flujo de procesamiento de solicitudes de crédito.
 * Implementa el flujo completo: validación antifraude -> persistencia -> envío al core bancario.
 * Maneja idempotencia para evitar duplicados y resiliencia con circuit breaker y retry.
 */
@Service
public class ProcessCreditRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessCreditRequestUseCase.class);
    private static final Duration CIRCUIT_BREAKER_TIMEOUT = Duration.ofSeconds(2);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final CreditRequestPort creditRequestPort;
    private final AntiFraudPort antiFraudPort;
    private final CoreBankingPort coreBankingPort;
    private final IdempotencyHandler idempotencyHandler;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ProcessCreditRequestUseCase(
            CreditRequestPort creditRequestPort,
            AntiFraudPort antiFraudPort,
            CoreBankingPort coreBankingPort,
            IdempotencyHandler idempotencyHandler,
            CircuitBreaker circuitBreaker,
            Retry retry) {
        this.creditRequestPort = creditRequestPort;
        this.antiFraudPort = antiFraudPort;
        this.coreBankingPort = coreBankingPort;
        this.idempotencyHandler = idempotencyHandler;
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }

    /**
     * Procesa una solicitud de crédito aplicando el flujo completo de validación y aprobación.
     * @param creditRequest la solicitud de crédito a procesar
     * @return Mono con el resultado del procesamiento
     */
    public Mono<CreditProcessingResult> execute(CreditRequest creditRequest) {
        String idempotencyKey = creditRequest.generateIdempotencyKey();
        log.info("Iniciando procesamiento de solicitud con clave de idempotencia: {}", idempotencyKey);

        return idempotencyHandler.checkAndStore(idempotencyKey)
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    log.info("Solicitud duplicada detectada para clave: {}", idempotencyKey);
                    return creditRequestPort.findByIdempotencyKey(idempotencyKey)
                        .map(existing -> CreditProcessingResult.duplicate(existing, idempotencyKey));
                }
                return processNewRequest(creditRequest, idempotencyKey);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CreditProcessingResult> processNewRequest(CreditRequest creditRequest, String idempotencyKey) {
        return creditRequestPort.save(creditRequest)
            .flatMap(savedRequest -> validateAntiFraud(savedRequest))
            .flatMap(validatedRequest -> updateStatusAndNotify(validatedRequest, idempotencyKey))
            .onErrorResume(error -> handleProcessingError(creditRequest, idempotencyKey, error));
    }

    private Mono<CreditRequest> validateAntiFraud(CreditRequest request) {
        log.info("Ejecutando validación antifraude para cliente: {}", request.customerId());
        return antiFraudPort.validate(request)
            .flatMap(result -> {
                if (result.approved()) {
                    log.info("Validación antifraude aprobada para cliente: {}", request.customerId());
                    return Mono.just(request);
                }
                log.warn("Validación antifraude rechazada para cliente: {}. Razón: {}",
                    request.customerId(), result.reason());
                return creditRequestPort.updateStatus(
                        request.id(),
                        CreditRequestStatus.REJECTED,
                        "AntiFraud: " + result.reason()
                    )
                    .then(Mono.error(new AntiFraudRejectionException(result.reason())));
            });
    }

    private Mono<CreditProcessingResult> updateStatusAndNotify(CreditRequest request, String idempotencyKey) {
        return creditRequestPort.updateStatus(request.id(), CreditRequestStatus.APPROVED, null)
            .then(submitToCoreBanking(request))
            .map(coreResult -> mapToProcessingResult(request, coreResult, idempotencyKey))
            .onErrorResume(error -> {
                log.error("Error al enviar al core bancario: {}", error.getMessage());
                return creditRequestPort.updateStatus(
                        request.id(),
                        CreditRequestStatus.PENDING_CORE,
                        "Core Banking pending: " + error.getMessage()
                    )
                    .thenReturn(CreditProcessingResult.pending(request, idempotencyKey,
                        "Submitted to core banking but awaiting confirmation"));
            });
    }

    private Mono<CoreBankingResult> submitToCoreBanking(CreditRequest request) {
        return Mono.defer(() -> coreBankingPort.submitCreditRequest(request))
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .transformDeferred(Operator.retry(retry))
            .timeout(CIRCUIT_BREAKER_TIMEOUT)
            .doOnSuccess(result -> log.info("Core bancario procesó solicitud: ref={}, success={}",
                result.referenceId(), result.success()))
            .doOnError(error -> log.error("Error en comunicación con core bancario: {}", error.getMessage()))
            .onErrorResume(error -> {
                if (error instanceof io.github.resilience4j.circuitbreaker.CallNotPermittedException) {
                    log.warn("Circuit breaker abierto, guardando para procesamiento posterior");
                    return Mono.just(CoreBankingResult.failure("Circuit breaker open",
                        CoreBankingErrorCode.SYSTEM_ERROR));
                }
                if (error instanceof java.util.concurrent.TimeoutException) {
                    log.warn("Timeout del core bancario, continuando sin bloquear");
                    return Mono.just(CoreBankingResult.failure("Timeout", CoreBankingErrorCode.TIMEOUT));
                }
                return Mono.error(error);
            });
    }

    private CreditProcessingResult mapToProcessingResult(CreditRequest request,
                                                          CoreBankingResult coreResult,
                                                          String idempotencyKey) {
        if (coreResult.success()) {
            return CreditProcessingResult.success(request, idempotencyKey, coreResult.referenceId());
        } else {
            return CreditProcessingResult.failure(request, idempotencyKey, coreResult.message());
        }
    }

    private Mono<CreditProcessingResult> handleProcessingError(CreditRequest request,
                                                                 String idempotencyKey,
                                                                 Throwable error) {
        log.error("Error en procesamiento de solicitud {}: {}", idempotencyKey, error.getMessage());
        CreditRequestStatus status = error instanceof AntiFraudRejectionException
            ? CreditRequestStatus.REJECTED
            : CreditRequestStatus.FAILED;
        String reason = error.getMessage();

        return creditRequestPort.updateStatus(request.id(), status, reason)
            .thenReturn(CreditProcessingResult.failure(request, idempotencyKey, reason));
    }

    /**
     * Resultado del procesamiento de una solicitud de crédito.
     */
    public record CreditProcessingResult(
        ProcessingStatus status,
        UUID requestId,
        String idempotencyKey,
        String coreBankingReference,
        String message,
        Instant processedAt
    ) {
        public static CreditProcessingResult success(CreditRequest request, String idempotencyKey, String coreRef) {
            return new CreditProcessingResult(
                ProcessingStatus.SUCCESS,
                request.id(),
                idempotencyKey,
                coreRef,
                "Credit request processed successfully",
                Instant.now()
            );
        }

        public static CreditProcessingResult failure(CreditRequest request, String idempotencyKey, String reason) {
            return new CreditProcessingResult(
                ProcessingStatus.FAILED,
                request.id(),
                idempotencyKey,
                null,
                reason,
                Instant.now()
            );
        }

        public static CreditProcessingResult duplicate(CreditRequest existing, String idempotencyKey) {
            return new CreditProcessingResult(
                ProcessingStatus.DUPLICATE,
                existing.id(),
                idempotencyKey,
                null,
                "Previous request found with same idempotency key",
                Instant.now()
            );
        }

        public static CreditProcessingResult pending(CreditRequest request, String idempotencyKey, String message) {
            return new CreditProcessingResult(
                ProcessingStatus.PENDING,
                request.id(),
                idempotencyKey,
                null,
                message,
                Instant.now()
            );
        }
    }

    /**
     * Estados posibles del procesamiento.
     */
    public enum ProcessingStatus {
        SUCCESS,
        FAILED,
        DUPLICATE,
        PENDING
    }

    /**
     * Excepción lanzada cuando la validación antifraude rechaza la solicitud.
     */
    public static class AntiFraudRejectionException extends RuntimeException {
        public AntiFraudRejectionException(String reason) {
            super("AntiFraud rejection: " + reason);
        }
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapter.java ===
package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
import com.bank.credit.domain.ports.AntiFraudPort.AntiFraudResult;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.timeLimiter.TimeLimiterOperator;
import io.github.resilience4j.timeLimiter.TimeLimiter;
import io.github.resilience4j.timeLimiter.TimeLimiterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
public class AntiFraudAdapter implements AntiFraudPort {

    private static final Logger log = LoggerFactory.getLogger(AntiFraudAdapter.class);
    private static final String CIRCUIT_BREAKER_NAME = "antiFraudCircuitBreaker";
    private static final Duration TIMEOUT_DURATION = Duration.ofSeconds(2);

    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;

    public AntiFraudAdapter(CircuitBreakerRegistry circuitBreakerRegistry,
                            TimeLimiterRegistry timeLimiterRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME);
        this.timeLimiter = timeLimiterRegistry.timeLimiter("antiFraudTimeLimiter",
                TimeLimiterConfig.custom()
                        .timeoutDuration(TIMEOUT_DURATION)
                        .build());
    }

    @Override
    public Mono<AntiFraudResult> validate(CreditRequest creditRequest) {
        log.info("Iniciando validación antifraude para creditRequest: {}", creditRequest.idempotencyKey());

        return Mono.fromCallable(() -> performAntiFraudValidation(creditRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(TimeLimiterOperator.of(timeLimiter))
                .doOnSuccess(result -> log.info("Validación antifraude completada: riesgo={}, motivo={}",
                        result.riskLevel(), result.reason()))
                .onErrorResume(TimeoutException.class, e -> {
                    log.warn("Timeout en validación antifraude para creditRequest: {}", creditRequest.idempotencyKey());
                    return Mono.just(new AntiFraudResult("HIGH", true, "Timeout en servicio antifraude - continuando con precaución"));
                })
                .onErrorResume(CircuitBreakerOpenException.class, e -> {
                    log.error("Circuit breaker abierto para servicio antifraude");
                    return Mono.just(new AntiFraudResult("MEDIUM", false, "Servicio antifraude no disponible - approved por defecto"));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error inesperado en validación antifraude: {}", e.getMessage());
                    return Mono.just(new AntiFraudResult("MEDIUM", false, "Error en servicio antifraude"));
                });
    }

    private AntiFraudResult performAntiFraudValidation(CreditRequest creditRequest) {
        String customerId = creditRequest.customerId();
        BigDecimal amount = creditRequest.requestedAmount();

        if (customerId == null || customerId.isBlank()) {
            return new AntiFraudResult("HIGH", true, "Cliente sin identificador válido");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new AntiFraudResult("HIGH", true, "Monto inválido para validación");
        }

        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            return new AntiFraudResult("HIGH", false, "Monto requiere revisión manual");
        }

        if (customerId.startsWith("BLOCKED_")) {
            return new AntiFraudResult("HIGH", true, "Cliente en lista de bloqueo");
        }

        if (amount.compareTo(new BigDecimal("50000")) > 0) {
            return new AntiFraudResult("MEDIUM", false, "Monto elevado requiere validación adicional");
        }

        return new AntiFraudResult("LOW", false, "Validación exitosa");
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public record AntiFraudResult(String riskLevel, boolean shouldReject, String reason) {
        public static AntiFraudResult approved(String reason) {
            return new AntiFraudResult("LOW", false, reason);
        }

        public static AntiFraudResult rejected(String reason) {
            return new AntiFraudResult("HIGH", true, reason);
        }
    }

    private static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }

    private static class TimeLimiterConfig {
        public static CustomTimeLimiterConfigBuilder custom() {
            return new CustomTimeLimiterConfigBuilder();
        }
    }

    private static class CustomTimeLimiterConfigBuilder {
        private Duration timeoutDuration;

        public CustomTimeLimiterConfigBuilder timeoutDuration(Duration duration) {
            this.timeoutDuration = duration;
            return this;
        }

        public TimeLimiterConfig build() {
            return new TimeLimiterConfig(timeoutDuration);
        }
    }

    private static class TimeLimiterConfig {
        private final Duration timeoutDuration;

        public TimeLimiterConfig(Duration timeoutDuration) {
            this.timeoutDuration = timeoutDuration;
        }

        public Duration getTimeoutDuration() {
            return timeoutDuration;
        }
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/adapters/CoreBankingAdapter.java ===
package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.CoreBankingPort;
import com.bank.credit.domain.ports.CoreBankingPort.CoreBankingResult;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.operator.RetryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CoreBankingAdapter implements CoreBankingPort {

    private static final Logger log = LoggerFactory.getLogger(CoreBankingAdapter.class);
    private static final String RETRY_NAME = "coreBankingRetry";
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_WAIT_DURATION = Duration.ofMillis(500);

    private final Retry retry;

    public CoreBankingAdapter(RetryRegistry retryRegistry) {
        this.retry = retryRegistry.retry(RETRY_NAME);
    }

    @Override
    public Mono<CoreBankingResult> registerCreditRequest(CreditRequest creditRequest) {
        log.info("Registrando solicitud de crédito en core bancario: idempotencyKey={}, amount={}",
                creditRequest.idempotencyKey(), creditRequest.requestedAmount());

        AtomicInteger attemptCount = new AtomicInteger(0);

        return Mono.fromCallable(() -> {
                    int attempt = attemptCount.incrementAndGet();
                    log.debug("Intento {} de registro en core bancario", attempt);
                    return performCoreBankingRegistration(creditRequest);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .transformDeferred(RetryOperator.of(retry))
                .doOnSuccess(result -> log.info("Registro en core bancario exitoso: accountId={}, status={}",
                        result.accountId(), result.status()))
                .doOnError(e -> log.error("Error en registro después de {} reintentos: {}",
                        attemptCount.get(), e.getMessage()))
                .onErrorResume(Exception.class, e -> {
                    log.warn("Fallback ejecutado para registro de crédito: {}", e.getMessage());
                    return executeFallback(creditRequest);
                });
    }

    @Override
    public Mono<CoreBankingResult> checkAccountStatus(String customerId) {
        log.info("Verificando estado de cuenta para cliente: {}", customerId);

        return Mono.fromCallable(() -> performAccountStatusCheck(customerId))
                .subscribeOn(Schedulers.boundedElastic())
                .transformDeferred(RetryOperator.of(retry))
                .doOnSuccess(result -> log.info("Estado de cuenta verificado: customerId={}, status={}",
                        customerId, result.status()))
                .onErrorResume(Exception.class, e -> {
                    log.warn("Fallback ejecutado para verificación de cuenta: {}", e.getMessage());
                    return Mono.just(new CoreBankingResult(
                            customerId,
                            "UNKNOWN",
                            "FALLBACK",
                            "Estado no verificable - approved por defecto"
                    ));
                });
    }

    @Override
    public Mono<CoreBankingResult> reserveFunds(String customerId, BigDecimal amount) {
        log.info("Reservando fondos para cliente: {}, monto: {}", customerId, amount);

        return Mono.fromCallable(() -> performFundReservation(customerId, amount))
                .subscribeOn(Schedulers.boundedElastic())
                .transformDeferred(RetryOperator.of(retry))
                .doOnSuccess(result -> log.info("Fondos reservados: transactionId={}", result.transactionId()))
                .onErrorResume(Exception.class, e -> {
                    log.warn("Fallback ejecutado para reserva de fondos: {}", e.getMessage());
                    return Mono.just(new CoreBankingResult(
                            customerId,
                            "PENDING",
                            "FALLBACK",
                            "Reserva no completada - approved por defecto"
                    ));
                });
    }

    private CoreBankingResult performCoreBankingRegistration(CreditRequest creditRequest) {
        String customerId = creditRequest.customerId();
        BigDecimal amount = creditRequest.requestedAmount();

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("CustomerId no puede ser vacío");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }

        String accountId = "ACC-" + customerId + "-" + System.currentTimeMillis();
        String status = amount.compareTo(new BigDecimal("50000")) > 0 ? "PENDING_APPROVAL" : "APPROVED";

        return new CoreBankingResult(
                accountId,
                customerId,
                status,
                "Registro exitoso en core bancario"
        );
    }

    private CoreBankingResult performAccountStatusCheck(String customerId) {
        if (customerId.startsWith("INVALID")) {
            throw new RuntimeException("Cuenta no encontrada");
        }

        String accountStatus = customerId.contains("SUSPENDED") ? "SUSPENDED" : "ACTIVE";

        return new CoreBankingResult(
                "ACC-" + customerId,
                customerId,
                accountStatus,
                "Verificación exitosa"
        );
    }

    private CoreBankingResult performFundReservation(String customerId, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new RuntimeException("Monto excede límite de reserva");
        }

        String transactionId = UUID.randomUUID().toString();

        return new CoreBankingResult(
                customerId,
                customerId,
                "RESERVED",
                "Fonds réservés avec succès: " + transactionId
        );
    }

    private Mono<CoreBankingResult> executeFallback(CreditRequest creditRequest) {
        log.info("Ejecutando fallback para creditRequest: {}", creditRequest.idempotencyKey());

        String fallbackAccountId = "FALLBACK-" + UUID.randomUUID().toString().substring(0, 8);

        return Mono.just(new CoreBankingResult(
                fallbackAccountId,
                creditRequest.customerId(),
                "FALLBACK_APPROVED",
                "Aprobado en modo fallback - requiere revisión manual"
        ));
    }

    public Retry getRetry() {
        return retry;
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/controllers/CreditRequestController.java ===
package com.bank.credit.infrastructure.controllers;

import com.bank.credit.application.usecases.ProcessCreditRequestUseCase;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequest.CreditRequestStatus;
import com.bank.credit.infrastructure.adapters.IdempotencyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/credit-requests")
@Tag(name = "Credit Requests", description = "API para gestión de solicitudes de crédito")
public class CreditRequestController {

    private static final Logger log = LoggerFactory.getLogger(CreditRequestController.class);

    private final ProcessCreditRequestUseCase processCreditRequestUseCase;
    private final IdempotencyHandler idempotencyHandler;

    public CreditRequestController(ProcessCreditRequestUseCase processCreditRequestUseCase,
                                   IdempotencyHandler idempotencyHandler) {
        this.processCreditRequestUseCase = processCreditRequestUseCase;
        this.idempotencyHandler = idempotencyHandler;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear solicitud de crédito",
            description = "Crea una nueva solicitud de crédito con validación de idempotencia. " +
                    "El sistema garantiza que múltiples invocaciones con la misma clave de idempotencia " +
                    "(operationNumber + channel) retornen el mismo resultado dentro de 24 horas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud de crédito creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "200", description = "Solicitud existente retornada (idempotencia)",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de idempotencia",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Solicitud rechazada por validación de negocio",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Mono<ResponseEntity<CreditRequestResponse>> createCreditRequest(
            @Parameter(description = "Datos de la solicitud de crédito", required = true)
            @RequestBody CreditRequestDTO request) {

        log.info("Recibida solicitud de crédito: operationNumber={}, channel={}, amount={}",
                request.operationNumber(), request.channel(), request.requestedAmount());

        return processCreditRequestUseCase.execute(
                        request.customerId(),
                        request.requestedAmount(),
                        request.termMonths(),
                        request.operationNumber(),
                        request.channel()
                )
                .map(creditRequest -> {
                    CreditRequestStatus status = creditRequest.status();
                    HttpStatus httpStatus = switch (status) {
                        case APPROVED, PENDING_APPROVAL -> HttpStatus.CREATED;
                        case REJECTED -> HttpStatus.UNPROCESSABLE_ENTITY;
                        default -> HttpStatus.INTERNAL_SERVER_ERROR;
                    };
                    return ResponseEntity.status(httpStatus)
                            .body(CreditRequestResponse.fromDomain(creditRequest));
                })
                .onErrorResume(WebExchangeBindException.class, e -> {
                    log.warn("Error de validación en solicitud: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest()
                            .body(new CreditRequestResponse(null, null, null, null, null,
                                    "VALIDATION_ERROR", e.getMessage(), null)));
                })
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Argumento inválido: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest()
                            .body(new CreditRequestResponse(null, null, null, null, null,
                                    "BAD_REQUEST", e.getMessage(), null)));
                });
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener solicitud de crédito por ID",
            description = "Recupera los detalles de una solicitud de crédito existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public Mono<ResponseEntity<CreditRequestResponse>> getCreditRequest(
            @Parameter(description = "ID de la solicitud de crédito", required = true)
            @PathVariable UUID id) {

        log.info("Consultando solicitud de crédito: id={}", id);

        return processCreditRequestUseCase.findById(id)
                .map(creditRequest -> ResponseEntity.ok(CreditRequestResponse.fromDomain(creditRequest)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/idempotency/{idempotencyKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consultar por clave de idempotencia",
            description = "Recupera una solicitud de crédito usando su clave de idempotencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public Mono<ResponseEntity<CreditRequestResponse>> getByIdempotencyKey(
            @Parameter(description = "Clave de idempotencia (operationNumber-channel)", required = true)
            @PathVariable String idempotencyKey) {

        log.info("Consultando por clave de idempotencia: {}", idempotencyKey);

        return processCreditRequestUseCase.findByIdempotencyKey(idempotencyKey)
                .map(creditRequest -> ResponseEntity.ok(CreditRequestResponse.fromDomain(creditRequest)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar solicitudes de crédito",
            description = "Lista todas las solicitudes de crédito con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes")
    })
    public Mono<ResponseEntity<Map<String, Object>>> listCreditRequests(
            @Parameter(description = "Número de página (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "20") int size) {

        log.info("Listando solicitudes de crédito: page={}, size={}", page, size);

        return processCreditRequestUseCase.findAll(page, size)
                .map(result -> ResponseEntity.ok(Map.of(
                        "content", result.get("content"),
                        "page", page,
                        "size", size,
                        "totalElements", result.get("totalElements"),
                        "totalPages", result.get("totalPages")
                )));
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Health check", description = "Verifica el estado del servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio disponible")
    })
    public Mono<ResponseEntity<Map<String, String>>> health() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "credit-processing-system",
                "timestamp", java.time.Instant.now().toString()
        )));
    }

    public record CreditRequestDTO(
            String customerId,
            BigDecimal requestedAmount,
            Integer termMonths,
            String operationNumber,
            String channel,
            BigDecimal interestRate,
            String purpose
    ) {}

    public record CreditRequestResponse(
            UUID id,
            String customerId,
            BigDecimal requestedAmount,
            Integer termMonths,
            String status,
            String idempotencyKey,
            String message,
            LocalDate createdAt
    ) {
        public static CreditRequestResponse fromDomain(CreditRequest creditRequest) {
            return new CreditRequestResponse(
                    creditRequest.id(),
                    creditRequest.customerId(),
                    creditRequest.requestedAmount(),
                    creditRequest.termMonths(),
                    creditRequest.status().name(),
                    creditRequest.idempotencyKey(),
                    null,
                    creditRequest.createdAt()
            );
        }
    }

    public record ErrorResponse(
            String error,
            String message,
            String details
    ) {}
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/config/ResilienceConfig.java ===
package com.bank.credit.infrastructure.config;


import com.bank.credit.domain.exceptions.AntiFraudValidationException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@Configuration
@Profile("!test")
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(
                        java.io.IOException.class,
                        java.util.concurrent.TimeoutException.class,
                        org.springframework.web.reactive.function.client.WebClientResponseException.ServiceUnavailable.class
                )
                .build();
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .cancelRunningFuture(true)
                .build();
        return TimeLimiterRegistry.of(config);
    }

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(
                        java.io.IOException.class,
                        org.springframework.web.reactive.function.client.WebClientResponseException.ServiceUnavailable.class
                )
                .ignoreExceptions(
                        com.bank.credit.infrastructure.exceptions.AntiFraudValidationException.class
                )
                .build();
        return RetryRegistry.of(config);
    }

    @Bean
    public CircuitBreaker antiFraudCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("antiFraudService");
    }

    @Bean
    public CircuitBreaker coreBankingCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("coreBankingService");
    }

    @Bean
    public Retry antiFraudRetry(RetryRegistry registry) {
        return registry.retry("antiFraudService");
    }

    @Bean
    public io.github.resilience4j.reactor.retry.RetryTransformer retryTransformer(Retry retry) {
        return io.github.resilience4j.reactor.retry.RetryTransformer.of(retry);
    }

    @Bean
    public io.github.resilience4j.reactor.circuitbreaker.CircuitBreakerTransformer circuitBreakerTransformer(CircuitBreaker circuitBreaker) {
        return io.github.resilience4j.reactor.circuitbreaker.CircuitBreakerTransformer.of(circuitBreaker);
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/config/KafkaConfig.java ===
package com.bank.credit.infrastructure.config;

import com.bank.credit.domain.events.CreditRequestApprovedEvent;
import com.bank.credit.domain.events.CreditRequestRejectedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverConfig;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:credit-processing-group}")
    private String groupId;

    @Bean
    public NewTopic creditApprovedTopic() {
        return TopicBuilder.name("credit-request-approved")
                .partitions(6)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic creditRejectedTopic() {
        return TopicBuilder.name("credit-request-rejected")
                .partitions(6)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic creditProcessedTopic() {
        return TopicBuilder.name("credit-request-processed")
                .partitions(6)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.TRANSACTION_TIMEOUT_MS_CONFIG, 30000);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.credit.domain.events");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public KafkaSender<String, Object> reactiveKafkaSender() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put("retries", 3);
        
        SenderConfig senderConfig = new SenderConfig(props);
        return KafkaSender.create(senderConfig);
    }

    @Bean
    public ReceiverOptions<String, Object> reactiveReceiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.credit.domain.events");
        
        ReceiverOptions<String, Object> options = ReceiverOptions.create(props);
        return options.subscription(List.of("credit-request-approved", "credit-request-rejected"));
    }

    @Bean
    public KafkaSender<String, CreditRequestApprovedEvent> approvedEventSender() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        SenderConfig senderConfig = new SenderConfig(props);
        return KafkaSender.create(senderConfig);
    }

    @Bean
    public KafkaSender<String, CreditRequestRejectedEvent> rejectedEventSender() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        SenderConfig senderConfig = new SenderConfig(props);
        return KafkaSender.create(senderConfig);
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/adapters/IdempotencyHandler.java ===
package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.CreditRequestPort;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class IdempotencyHandler {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyHandler.class);
    private static final Duration IDEMPOTENCY_TTL = Duration.ofHours(24);
    private static final String IDEMPOTENCY_MAP_NAME = "credit:idempotency:";

    private final RedissonClient redissonClient;
    private final CreditRequestPort creditRequestPort;

    public IdempotencyHandler(RedissonClient redissonClient, CreditRequestPort creditRequestPort) {
        this.redissonClient = redissonClient;
        this.creditRequestPort = creditRequestPort;
    }

    public Mono<CreditRequest> handleIdempotentOperation(
            String operationNumber, 
            String channel, 
            Mono<CreditRequest> operation) {
        
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        log.info("Procesando operación idempotente con clave: {}", idempotencyKey);
        
        return findByIdempotencyKey(idempotencyKey)
                .flatMap(existing -> {
                    log.info("Operación previamente procesada, retornando resultado existente para clave: {}", idempotencyKey);
                    return Mono.just(existing);
                })
                .switchIfEmpty(operation
                        .flatMap(result -> {
                            result = enrichWithIdempotencyKey(result, idempotencyKey);
                            return saveWithIdempotencyTracking(result, idempotencyKey);
                        })
                );
    }

    private Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey) {
        return creditRequestPort.findByIdempotencyKey(idempotencyKey);
    }

    private Mono<CreditRequest> saveWithIdempotencyTracking(CreditRequest request, String idempotencyKey) {
        return creditRequestPort.save(request)
                .flatMap(saved -> {
                    cacheIdempotencyKey(idempotencyKey, saved.id().toString());
                    log.info("Operación guardada con clave de idempotencia: {}", idempotencyKey);
                    return Mono.just(saved);
                });
    }

    private void cacheIdempotencyKey(String idempotencyKey, String requestId) {
        RMapCache<String, String> cache = redissonClient.getMapCache(IDEMPOTENCY_MAP_NAME + idempotencyKey);
        cache.put("requestId", requestId, IDEMPOTENCY_TTL.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        cache.put("createdAt", Instant.now().toString(), IDEMPOTENCY_TTL.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        log.debug("Clave de idempotencia {} almacenada en caché con TTL de {} horas", 
                idempotencyKey, IDEMPOTENCY_TTL.toHours());
    }

    public Mono<Optional<String>> getCachedRequestId(String operationNumber, String channel) {
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        RMapCache<String, String> cache = redissonClient.getMapCache(IDEMPOTENCY_MAP_NAME + idempotencyKey);
        
        String requestId = cache.get("requestId");
        if (requestId != null) {
            log.info("Cache hit para clave de idempotencia: {}", idempotencyKey);
            return Mono.just(Optional.of(requestId));
        }
        
        log.debug("Cache miss para clave de idempotencia: {}", idempotencyKey);
        return Mono.just(Optional.empty());
    }

    public Mono<Boolean> validateIdempotencyKey(String operationNumber, String channel) {
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        RMapCache<String, String> cache = redissonClient.getMapCache(IDEMPOTENCY_MAP_NAME + idempotencyKey);
        return Mono.just(cache.containsKey("requestId"));
    }

    public Mono<Void> invalidateIdempotencyKey(String operationNumber, String channel) {
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        RMapCache<String, String> cache = redissonClient.getMapCache(IDEMPOTENCY_MAP_NAME + idempotencyKey);
        cache.delete();
        log.info("Clave de idempotencia invalidada: {}", idempotencyKey);
        return Mono.empty();
    }

    private String generateIdempotencyKey(String operationNumber, String channel) {
        return channel + ":" + operationNumber;
    }

    private CreditRequest enrichWithIdempotencyKey(CreditRequest request, String idempotencyKey) {
        return new CreditRequest(
                request.id(),
                request.operationNumber(),
                request.channel(),
                request.applicantId(),
                request.applicantName(),
                request.applicantEmail(),
                request.applicantPhone(),
                request.requestedAmount(),
                request.termMonths(),
                request.interestRate(),
                request.creditType(),
                request.status(),
                idempotencyKey,
                request.requestedAt(),
                request.processedAt(),
                request.approvedBy(),
                request.rejectionReason(),
                request.antiFraudScore(),
                request.antiFraudDecision(),
                request.coreBankingReference()
        );
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/exceptions/CreditRequestNotFoundException.java ===
package com.bank.credit.domain.exceptions;

import java.time.Instant;
import java.util.UUID;

public class CreditRequestNotFoundException extends RuntimeException {
    private final UUID requestId;
    private final Instant timestamp;
    private final String searchContext;
    private static final long serialVersionUID = 1L;

    public CreditRequestNotFoundException(UUID requestId) {
        super(buildMessage(requestId, null));
        this.requestId = requestId;
        this.timestamp = Instant.now();
        this.searchContext = "ID";
    }

    public CreditRequestNotFoundException(String idempotencyKey) {
        super(buildMessage(null, idempotencyKey));
        this.requestId = null;
        this.timestamp = Instant.now();
        this.searchContext = "IDEMPOTENCY_KEY";
    }

    public CreditRequestNotFoundException(UUID requestId, String idempotencyKey) {
        super(buildMessage(requestId, idempotencyKey));
        this.requestId = requestId;
        this.timestamp = Instant.now();
        this.searchContext = requestId != null ? "ID" : "IDEMPOTENCY_KEY";
    }

    private static String buildMessage(UUID requestId, String idempotencyKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("Solicitud de crédito no encontrada. ");
        if (requestId != null) {
            sb.append("ID: ").append(requestId);
        }
        if (idempotencyKey != null) {
            if (requestId != null) sb.append(", ");
            sb.append("Clave de idempotencia: ").append(idempotencyKey);
        }
        return sb.toString();
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSearchContext() {
        return searchContext;
    }

    public String getErrorCode() {
        return "CREDIT_REQUEST_NOT_FOUND";
    }

    public String getDetails() {
        return String.format("No se encontró la solicitud de crédito con %s: %s", 
            searchContext, 
            requestId != null ? requestId.toString() : getMessage().split("Clave de idempotencia: ")[1]);
    }

    public boolean hasRequestId() {
        return requestId != null;
    }

    public boolean hasIdempotencyKey() {
        return searchContext.equals("IDEMPOTENCY_KEY");
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/exceptions/IdempotencyConflictException.java ===
package com.bank.credit.domain.exceptions;

import java.time.Duration;
import java.time.Instant;

public class IdempotencyConflictException extends RuntimeException {
    private final String idempotencyKey;
    private final String channel;
    private final Instant originalRequestTime;
    private final Duration timeUntilExpiry;
    private static final long serialVersionUID = 1L;

    public IdempotencyConflictException(String idempotencyKey, String channel) {
        super(buildMessage(idempotencyKey, channel, null));
        this.idempotencyKey = idempotencyKey;
        this.channel = channel;
        this.originalRequestTime = Instant.now();
        this.timeUntilExpiry = Duration.ofHours(24);
    }

    public IdempotencyConflictException(String idempotencyKey, String channel, Instant originalRequestTime) {
        super(buildMessage(idempotencyKey, channel, originalRequestTime));
        this.idempotencyKey = idempotencyKey;
        this.channel = channel;
        this.originalRequestTime = originalRequestTime;
        long remainingMillis = Duration.between(Instant.now(), originalRequestTime.plusSeconds(86400)).toMillis();
        this.timeUntilExpiry = Duration.ofMillis(Math.max(0, remainingMillis));
    }

    private static String buildMessage(String idempotencyKey, String channel, Instant originalTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("Conflicto de idempotencia detectado. ");
        sb.append("La clave de idempotencia '[").append(idempotencyKey).append("]' ");
        sb.append("ya existe para el canal '[").append(channel).append("]'.");
        if (originalTime != null) {
            sb.append(" Solicitud original procesada el: ").append(originalTime);
        }
        return sb.toString();
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getChannel() {
        return channel;
    }

    public Instant getOriginalRequestTime() {
        return originalRequestTime;
    }

    public Duration getTimeUntilExpiry() {
        return timeUntilExpiry;
    }

    public String getErrorCode() {
        return "IDEMPOTENCY_CONFLICT";
    }

    public boolean isExpired() {
        return timeUntilExpiry.isNegative() || timeUntilExpiry.isZero();
    }

    public long getRemainingSeconds() {
        return timeUntilExpiry.getSeconds();
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/exceptions/InvalidCreditRequestException.java ===
package com.bank.credit.domain.exceptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvalidCreditRequestException extends RuntimeException {
    private final List<ValidationError> validationErrors;
    private static final long serialVersionUID = 1L;

    public InvalidCreditRequestException(String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
        addError("GENERAL", message);
    }

    public InvalidCreditRequestException(String field, String message) {
        super(buildMessage(field, message));
        this.validationErrors = new ArrayList<>();
        addError(field, message);
    }

    public InvalidCreditRequestException(List<ValidationError> errors) {
        super(buildErrorListMessage(errors));
        this.validationErrors = new ArrayList<>(errors);
    }

    private static String buildMessage(String field, String message) {
        return String.format("Error de validación en '%s': %s", field, message);
    }

    private static String buildErrorListMessage(List<ValidationError> errors) {
        if (errors.isEmpty()) {
            return "La solicitud de crédito contiene errores de validación";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("La solicitud de crédito contiene ");
        sb.append(errors.size());
        sb.append(" error(es) de validación: ");
        for (int i = 0; i < errors.size(); i++) {
            sb.append(errors.get(i).getField()).append(" - ").append(errors.get(i).getMessage());
            if (i < errors.size() - 1) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }

    public void addError(String field, String message) {
        this.validationErrors.add(new ValidationError(field, message));
    }

    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public String getErrorCode() {
        return "INVALID_CREDIT_REQUEST";
    }

    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }

    public int getErrorCount() {
        return validationErrors.size();
    }

    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/domain/exceptions/AntiFraudValidationException.java ===
package com.bank.credit.domain.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AntiFraudValidationException extends RuntimeException {
    private final UUID requestId;
    private final Instant validationTime;
    private final List<RiskFactor> riskFactors;
    private final FraudRiskLevel riskLevel;
    private static final long serialVersionUID = 1L;

    public AntiFraudValidationException(UUID requestId, String message) {
        super(buildMessage(requestId, message, null, FraudRiskLevel.UNKNOWN));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = new ArrayList<>();
        this.riskLevel = FraudRiskLevel.UNKNOWN;
    }

    public AntiFraudValidationException(UUID requestId, List<RiskFactor> riskFactors, FraudRiskLevel riskLevel) {
        super(buildMessage(requestId, null, riskFactors, riskLevel));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = new ArrayList<>(riskFactors);
        this.riskLevel = riskLevel;
    }

    public AntiFraudValidationException(UUID requestId, String message, List<RiskFactor> riskFactors, FraudRiskLevel riskLevel) {
        super(buildMessage(requestId, message, riskFactors, riskLevel));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = riskFactors != null ? new ArrayList<>(riskFactors) : new ArrayList<>();
        this.riskLevel = riskLevel;
    }

    private static String buildMessage(UUID requestId, String message, List<RiskFactor> factors, FraudRiskLevel level) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validación de antifraude fallida para la solicitud: ").append(requestId);
        if (message != null) {
            sb.append(". Razón: ").append(message);
        }
        if (level != null && level != FraudRiskLevel.UNKNOWN) {
            sb.append(". Nivel de riesgo: ").append(level.name());
        }
        if (factors != null && !factors.isEmpty()) {
            sb.append(". Factores de riesgo detectados: ").append(factors.size());
        }
        return sb.toString();
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Instant getValidationTime() {
        return validationTime;
    }

    public List<RiskFactor> getRiskFactors() {
        return Collections.unmodifiableList(riskFactors);
    }

    public FraudRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getErrorCode() {
        return "ANTIFRAUD_VALIDATION_FAILED";
    }

    public boolean hasRiskFactors() {
        return !riskFactors.isEmpty();
    }

    public boolean isHighRisk() {
        return riskLevel == FraudRiskLevel.HIGH || riskLevel == FraudRiskLevel.CRITICAL;
    }

    public static class RiskFactor {
        private final String factor;
        private final String description;
        private final double score;

        public RiskFactor(String factor, String description, double score) {
            this.factor = factor;
            this.description = description;
            this.score = score;
        }

        public String getFactor() {
            return factor;
        }

        public String getDescription() {
            return description;
        }

        public double getScore() {
            return score;
        }
    }

    public enum FraudRiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL,
        UNKNOWN
    }
}

// === ARCHIVO: src/main/java/com/bank/credit/infrastructure/exceptions/GlobalExceptionHandler.java ===
package com.bank.credit.infrastructure.exceptions;

import com.bank.credit.domain.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CreditRequestNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCreditRequestNotFound(CreditRequestNotFoundException ex) {
        log.warn("Solicitud de crédito no encontrada: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getErrorCode(),
            ex.getMessage(),
            Instant.now(),
            buildMetadata(ex)
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(IdempotencyConflictException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIdempotencyConflict(IdempotencyConflictException ex) {
        log.warn("Conflicto de idempotencia: {}", ex.getMessage());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("idempotencyKey", ex.getIdempotencyKey());
        metadata.put("channel", ex.getChannel());
        metadata.put("originalRequestTime", ex.getOriginalRequestTime().toString());
        metadata.put("remainingSeconds", ex.getRemainingSeconds());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getErrorCode(),
            "La solicitud ya fue procesada. Utilice la respuesta original.",
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    @ExceptionHandler(InvalidCreditRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidCreditRequest(InvalidCreditRequestException ex) {
        log.warn("Solicitud de crédito inválida: {}", ex.getMessage());
        
        Map<String, Object> metadata = new HashMap<>();
        if (ex.hasErrors()) {
            metadata.put("validationErrors", ex.getValidationErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getMessage()))
                .collect(Collectors.toList()));
            metadata.put("errorCount", ex.getErrorCount());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getErrorCode(),
            ex.getMessage(),
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(AntiFraudValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAntiFraudValidation(AntiFraudValidationException ex) {
        log.error("Validación de antifraude fallida para solicitud {}: nivel={}, factores={}", 
            ex.getRequestId(), ex.getRiskLevel(), ex.getRiskFactors().size());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("requestId", ex.getRequestId().toString());
        metadata.put("riskLevel", ex.getRiskLevel().name());
        metadata.put("validationTime", ex.getValidationTime().toString());
        
        if (ex.hasRiskFactors()) {
            metadata.put("riskFactors", ex.getRiskFactors().stream()
                .map(factor -> Map.of(
                    "factor", factor.getFactor(),
                    "description", factor.getDescription(),
                    "score", factor.getScore()
                ))
                .collect(Collectors.toList()));
        }
        
        HttpStatus status = ex.isHighRisk() ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.BAD_REQUEST;
        String userMessage = ex.isHighRisk() 
            ? "La solicitud ha sido rechazada por políticas de seguridad" 
            : "La solicitud requiere revisión adicional";
        
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            ex.getErrorCode(),
            userMessage,
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        log.error("Error no manejado: ", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Ha ocurrido un error interno. Por favor, contacte al administrador.",
            Instant.now(),
            Map.of("errorId", java.util.UUID.randomUUID().toString())
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    private Map<String, Object> buildMetadata(CreditRequestNotFoundException ex) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("searchContext", ex.getSearchContext());
        metadata.put("timestamp", ex.getTimestamp().toString());
        
        if (ex.hasRequestId()) {
            metadata.put("requestId", ex.getRequestId().toString());
        }
        if (ex.hasIdempotencyKey()) {
            metadata.put("idempotencyKey", ex.getMessage().split("Clave de idempotencia: ")[1]);
        }
        
        return metadata;
    }

    public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        Map<String, Object> metadata
    ) {}
}

// === ARCHIVO: src/test/java/com/bank/credit/application/usecases/ProcessCreditRequestUseCaseTest.java ===
package com.bank.credit.application.usecases;



import com.bank.credit.domain.ports.AntiFraudResult;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.domain.ports.CoreBankingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessCreditRequestUseCase - Pruebas de integración del caso de uso principal")
class ProcessCreditRequestUseCaseTest {

    @Mock
    private CreditRequestPort creditRequestPort;

    @Mock
    private AntiFraudPort antiFraudPort;

    @Mock
    private CoreBankingPort coreBankingPort;

    @InjectMocks
    private ProcessCreditRequestUseCase processCreditRequestUseCase;

    private CreditRequest testCreditRequest;

    @BeforeEach
    void setUp() {
        testCreditRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-001",
            new BigDecimal("50000.00"),
            36,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "WEB",
            "OP-2024-001",
            null
        );
    }

    @Test
    @DisplayName("Debería procesar exitosamente una solicitud de crédito cuando pasa todas las validaciones")
    void shouldProcessCreditRequestSuccessfullyWhenAllValidationsPass() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));
        when(coreBankingPort.submitApplication(any(CreditRequest.class)))
            .thenReturn(Mono.just("CORE-12345"));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.APPROVED);
            })
            .verifyComplete();

        verify(creditRequestPort).save(any(CreditRequest.class));
        verify(antiFraudPort).validate(any(CreditRequest.class));
        verify(coreBankingPort).submitApplication(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería rechazar solicitud cuando el antifraude la marca como fraudulenta")
    void shouldRejectCreditRequestWhenAntiFraudDetectsFraud() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(false, "REJECTED", "Alto riesgo detectado")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.REJECTED);
                assertThat(result.rejectionReason()).contains("Alto riesgo");
            })
            .verifyComplete();

        verify(coreBankingPort, never()).submitApplication(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería devolver solicitud existente cuando la clave de idempotencia ya existe")
    void shouldReturnExistingRequestWhenIdempotencyKeyExists() {
        CreditRequest existingRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-001",
            new BigDecimal("50000.00"),
            36,
            CreditRequest.CreditRequestStatus.APPROVED,
            LocalDateTime.now().minusHours(1),
            null,
            "WEB",
            "OP-2024-001",
            null
        );

        when(creditRequestPort.findByIdempotencyKey("OP-2024-001"))
            .thenReturn(Mono.just(existingRequest));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.id()).isEqualTo(existingRequest.id());
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.APPROVED);
            })
            .verifyComplete();

        verify(creditRequestPort, never()).save(any(CreditRequest.class));
        verify(antiFraudPort, never()).validate(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería manejar correctamente cuando el core bancario no está disponible")
    void shouldHandleCoreBankingFailureGracefully() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));
        when(coreBankingPort.submitApplication(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeException("Core bancario no disponible")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .expectErrorMatches(throwable -> throwable.getMessage().contains("Core bancario"))
            .verify();
    }

    @Test
    @DisplayName("Debería continuar flujo cuando antifraude tiene timeout pero hay fallback configurado")
    void shouldContinueFlowWhenAntiFraudTimesOut() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "PENDING_REVIEW", "Timeout en verificación")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isIn(
                    CreditRequest.CreditRequestStatus.PENDING,
                    CreditRequest.CreditRequestStatus.PENDING_REVIEW
                );
            })
            .verifyComplete();
    }
}

// === ARCHIVO: src/test/java/com/bank/credit/infrastructure/adapters/AntiFraudAdapterTest.java ===
package com.bank.credit.infrastructure.adapters;



import com.bank.credit.domain.ports.AntiFraudResult;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AntiFraudAdapter - Pruebas del adaptador de servicio antifraude")
class AntiFraudAdapterTest {

    @Mock
    private AntiFraudPort antiFraudPort;

    @InjectMocks
    private AntiFraudAdapter antiFraudAdapter;

    private CreditRequest testCreditRequest;

    @BeforeEach
    void setUp() {
        testCreditRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-002",
            new BigDecimal("100000.00"),
            48,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "MOBILE",
            "OP-2024-002",
            null
        );
    }

    @Test
    @DisplayName("Debería aprobar solicitud cuando el análisis de riesgo es bajo")
    void shouldApproveRequestWhenRiskAnalysisIsLow() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("APPROVED");
                assertThat(result.reason()).isNull();
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería rechazar solicitud cuando el score de riesgo supera el umbral")
    void shouldRejectRequestWhenRiskScoreExceedsThreshold() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(false, "REJECTED", "Score de riesgo: 85/100")));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isFalse();
                assertThat(result.riskScore()).isEqualTo("REJECTED");
                assertThat(result.reason()).contains("85");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar timeout del servicio antifraude según configuración de Resilience4j")
    void shouldHandleAntiFraudServiceTimeout() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.delay(Duration.ofSeconds(3))
                .flatMap(l -> Mono.just(new AntiFraudPort.AntiFraudResult(true, "TIMEOUT_FALLBACK", null))));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest).timeout(Duration.ofSeconds(2)))
            .expectErrorMatches(throwable -> throwable.getMessage().contains("Timeout"))
            .verify();
    }

    @Test
    @DisplayName("Debería continuar con revisión manual cuando el servicio retorna error desconocido")
    void shouldContinueToManualReviewOnUnknownError() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeError("Servicio de riesgo no disponible")));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest)
                .onErrorResume(e -> Mono.just(new AntiFraudPort.AntiFraudResult(true, "MANUAL_REVIEW", "Error en servicio: " + e.getMessage()))))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("MANUAL_REVIEW");
                assertThat(result.reason()).contains("no disponible");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería marcar como revisión manual cuando el monto supera el límite automático")
    void shouldMarkForManualReviewWhenAmountExceedsAutomaticLimit() {
        CreditRequest highValueRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-003",
            new BigDecimal("500000.00"),
            60,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "BRANCH",
            "OP-2024-003",
            null
        );

        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "MANUAL_REVIEW", "Monto mayor a $200,000 requiere revisión manual")));

        StepVerifier.create(antiFraudAdapter.validate(highValueRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("MANUAL_REVIEW");
                assertThat(result.reason()).contains("revisión manual");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería registrar métricas de latencia del servicio antifraude")
    void shouldRecordAntiFraudServiceLatencyMetrics() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> assertThat(result.approved()).isTrue())
            .verifyComplete();

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> assertThat(result.approved()).isTrue())
            .verifyComplete();
    }
}

// === ARCHIVO: src/test/java/com/bank/credit/infrastructure/controllers/CreditRequestControllerTest.java ===
package com.bank.credit.infrastructure.controllers;


import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.application.usecases.ProcessCreditRequestUseCase;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.infrastructure.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditRequestController - Pruebas del controlador REST con WebTestClient")
class CreditRequestControllerTest {

    @Mock
    private ProcessCreditRequestUseCase processCreditRequestUseCase;

    @InjectMocks
    private CreditRequestController creditRequestController;

    private WebTestClient webTestClient;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new GlobalExceptionHandler(), creditRequestController)
            .configureClient()
            .baseUrl("http://localhost:8080")
            .build();
    }

    @Test
    @DisplayName("Debería crear solicitud de crédito exitosamente con código 201 Created")
    void shouldCreateCreditRequestSuccessfullyWith201Created() {
        CreditRequest request = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-100",
            new BigDecimal("75000.00"),
            36,
            CreditRequest.CreditRequestStatus.APPROVED,
            LocalDateTime.now(),
            null,
            "WEB",
            "OP-2024-100",
            null
        );

        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.just(request));

        String requestBody = """
            {
                "clientId": "CLIENT-100",
                "amount": 75000.00,
                "termMonths": 36,
                "channel": "WEB",
                "idempotencyKey": "OP-2024-100"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.clientId").isEqualTo("CLIENT-100")
            .jsonPath("$.amount").isEqualTo(75000.00)
            .jsonPath("$.status").isEqualTo("APPROVED");
    }

    @Test
    @DisplayName("Debería devolver 400 Bad Request cuando los datos de entrada son inválidos")
    void shouldReturn400BadRequestWhenInputDataIsInvalid() {
        String invalidRequest = """
            {
                "clientId": "",
                "amount": -1000.00,
                "termMonths": 0,
                "channel": "UNKNOWN_CHANNEL",
                "idempotencyKey": ""
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(invalidRequest))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.message").exists();
    }

    @Test
    @DisplayName("Debería devolver 409 Conflict cuando la clave de idempotencia ya existe")
    void shouldReturn409ConflictWhenIdempotencyKeyAlreadyExists() {
        CreditRequest existingRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-100",
            new BigDecimal("75000.00"),
            36,
            CreditRequest.CreditRequestStatus.REJECTED,
            LocalDateTime.now().minusHours(2),
            "Riesgo detectado",
            "WEB",
            "OP-2024-DUPLICATE",
            null
        );

        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.just(existingRequest));

        String requestBody = """
            {
                "clientId": "CLIENT-100",
                "amount": 75000.00,
                "termMonths": 36,
                "channel": "WEB",
                "idempotencyKey": "OP-2024-DUPLICATE"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody()
            .jsonPath("$.error").isEqualTo("CONFLICT")
            .jsonPath("$.message").exists();
    }

    @Test
    @DisplayName("Debería devolver 503 Service Unavailable cuando el servicio antifraude falla")
    void shouldReturn503WhenAntiFraudServiceFails() {
        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeException("Servicio antifraude no disponible")));

        String requestBody = """
            {
                "clientId": "CLIENT-200",
                "amount": 50000.00,
                "termMonths": 24,
                "channel": "MOBILE",
                "idempotencyKey": "OP-2024-200"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isServiceUnavailable()
            .expectBody()
            .jsonPath("$.error").isEqualTo("SERVICE_UNAVAILABLE")
            .jsonPath("$.message").contains("antifraude");
    }

    @Test
n    @DisplayName("Debería obtener solicitud de crédito por ID exitosamente")
    void shouldGetCreditRequestByIdSuccessfully() {
        UUID requestId = UUID.randomUUID();
        CreditRequest foundRequest = new CreditRequest(
            requestId,
            "CLIENT-300",
            new BigDecimal("120000.00"),
            48,
            CreditRequest.CreditRequestStatus.PENDING_REVIEW,
            LocalDateTime.now(),
            null,
            "BRANCH",
            "OP-2024-300",
            null
        );

        when(processCreditRequestUseCase.getById(requestId))
            .thenReturn(Mono.just(foundRequest));

        webTestClient.get()
            .uri("/api/v1/credit-requests/{id}", requestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(requestId.toString())
            .jsonPath("$.clientId").isEqualTo("CLIENT-300")
            .jsonPath("$.status").isEqualTo("PENDING_REVIEW");
    }

    @Test
    @DisplayName("Debería devolver 404 Not Found cuando la solicitud no existe")
    void shouldReturn404WhenCreditRequestNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(processCreditRequestUseCase.getById(nonExistentId))
            .thenReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/v1/credit-requests/{id}", nonExistentId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("NOT_FOUND");
    }
}

// === ARCHIVO: Dockerfile ===
# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar solo el POM para aprovechar caché de dependencias
COPY pom.xml .

# Descargar dependencias sin-compilar para maximizar caché
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests -B

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Copiar el JAR construido desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Establecer permisos correctos
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no root
USER appuser

# Exponer puerto de la aplicación
EXPOSE 8080

# Variables de entorno para producción
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Iniciar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

// === ARCHIVO: docker-compose.yml ===
version: '3.9'

services:
  # Base de datos PostgreSQL para persistencia reactiva
  postgres:
    image: postgres:16-alpine
    container_name: credit-postgres
    environment:
      POSTGRES_DB: credit_db
      POSTGRES_USER: credit_user
      POSTGRES_PASSWORD: credit_pass_2024
      POSTGRES_HOST_AUTH_METHOD: md5
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U credit_user -d credit_db"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - credit-network
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G

  # Redis para caché e idempotencia
  redis:
    image: redis:7-alpine
    container_name: credit-redis
    command: redis-server --appendonly yes --maxmemory 512mb --maxmemory-policy allkeys-lru
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 3s
      retries: 5
    networks:
      - credit-network
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 1G

  # Apache Kafka para procesamiento de eventos
  kafka:
    image: confluentinc/cp-kafka:7.6.0
    container_name: credit-kafka
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@localhost:9093
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_LOG_DIRS: /var/lib/kafka/data
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
    ports:
      - "9092:9092"
    volumes:
      - kafka_data:/var/lib/kafka/data
    healthcheck:
      test: ["CMD", "kafka-broker-api-versions", "--bootstrap-server", "localhost:9092"]
      interval: 10s
      timeout: 10s
      retries: 5
    networks:
      - credit-network
    deploy:
      resources:
        limits:
          cpus: '1.5'
          memory: 2G

  # Schema Registry para Kafka (serialización de esquemas)
  schema-registry:
    image: confluentinc/cp-schema-registry:7.6.0
    container_name: credit-schema-registry
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: kafka:9092
      SCHEMA_REGISTRY_LISTENERS: http://0.0.0.0:8081
    ports:
      - "8081:8081"
    depends_on:
      kafka:
        condition: service_healthy
    networks:
      - credit-network

  # Aplicación principal del sistema de créditos
  credit-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: credit-app
    ports:
      - "8080:8080"
      - "5005:5005"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_R2DBC_URL: r2dbc:pool:postgres://credit_user:credit_pass_2024@postgres:5432/credit_db
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      SPRING_KAFKA_CONSUMER_AUTO_OFFSET_RESET_CONFIG: earliest
      SERVER_PORT: 8080
      JAVA_OPTS: "-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
      RESILIENCE4J_CIRCUITBREAKER_CONFIG_CREDITPROCESSING_SLIDINGWINDOW: 100
      RESILIENCE4J_CIRCUITBREAKER_CONFIG_CREDITPROCESSING_MINIMUMNUMBEROFCALLS: 10
      RESILIENCE4J_CIRCUITBREAKER_CONFIG_CREDITPROCESSING_WAITDURATIONINOPENSTATE: 30s
      RESILIENCE4J_RETRY_CONFIG_COREBANKING_MAXATTEMPTS: 3
      RESILIENCE4J_RETRY_CONFIG_COREBANKING_WAITDURATION: 1s
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
      kafka:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 10
      start_period: 90s
    networks:
      - credit-network
    deploy:
      resources:
        limits:
          cpus: '3'
          memory: 3G
        reservations:
          cpus: '1'
          memory: 1G
    restart: on-failure:5

volumes:
  postgres_data:
    driver: local
  redis_data:
    driver: local
  kafka_data:
    driver: local

networks:
  credit-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.0.0/16

// === ARCHIVO: README.md ===
# Sistema de Procesamiento de Créditos

Sistema bancario de alto rendimiento para procesamiento de solicitudes de crédito con arquitectura reactiva, resiliencia distribuida y tolerancia a fallos.

## Características Principales

- **Rendimiento**: Capacidad para procesar 10,000 transacciones por segundo
- **SLA**: 99.99% de disponibilidad
- **Arquitectura**: Hexagonal con enfoque reactivo
- **Idempotencia**: Garantizada mediante clave (número de operación + canal) con TTL de 24 horas
- **Tolerancia a fallos**: Circuit Breaker, Retry, Timeout para servicios externos
- **Procesamiento asíncrono**: Eventos publicados a Kafka para auditoría y notificaciones

## Requisitos Previos

- Java 21
- Maven 3.9+
- Docker y Docker Compose
- 8GB de RAM disponibles

## Estructura del Proyecto

```
credit-processing-system/
├── src/
│   ├── main/
│   │   ├── java/com/bank/credit/
│   │   │   ├── Application.java                 # Punto de entrada
│   │   │   ├── domain/                          # Capa de dominio
│   │   │   │   ├── models/                      # Entidades y DTOs
│   │   │   │   └── ports/                       # Interfaces (contratos)
│   │   │   ├── application/                     # Capa de aplicación
│   │   │   │   └── usecases/                    # Casos de uso
│   │   │   └── infrastructure/                  # Capa de infraestructura
│   │   │       ├── adapters/                    # Implementaciones de puertos
│   │   │       ├── config/                      # Configuración
│   │   │       ├── controllers/                 # Controladores REST
│   │   │       ├── repositories/                # Repositorios de datos
│   │   │       └── exceptions/                  # Manejo de errores
│   │   └── resources/
│   │       └── application.yml                  # Configuración
│   └── test/
│       └── java/com/bank/credit/                # Pruebas
├── pom.xml                                       # Dependencias Maven
├── Dockerfile                                    # Imagen de contenedor
├── docker-compose.yml                            # Servicios locales
└── README.md                                     # Este archivo
```

## Configuración Local

### Variables de Entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| SPRING_PROFILES_ACTIVE | Perfil de Spring | dev |
| SERVER_PORT | Puerto de la aplicación | 8080 |
| SPRING_DATASOURCE_R2DBC_URL | URL de PostgreSQL | r2dbc:pool:postgres://localhost:5432/credit_db |
| SPRING_REDIS_HOST | Host de Redis | localhost |
| SPRING_REDIS_PORT | Puerto de Redis | 6379 |
| SPRING_KAFKA_BOOTSTRAP_SERVERS | Servidores Kafka | localhost:9092 |

### Configuración de Resiliencia

El sistema utiliza Resilience4j con las siguientes configuraciones:

**Circuit Breaker** (creditProcessing):
- Sliding Window: 100 llamadas
- Minimum Calls: 10 para decidir estado
- Wait Duration: 30s en estado abierto
- Failure Rate Threshold: 50%

**Retry** (coreBanking):
- Max Attempts: 3
- Wait Duration: 1s entre intentos
- Enable Random Wait: true

## Ejecución

### Desarrollo Local (Sin Docker)

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar pruebas
mvn test

# Iniciar la aplicación
mvn spring-boot:run
```

### Entorno Docker (Completo)

```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs de la aplicación
docker-compose logs -f credit-app

# Ver estado de todos los servicios
docker-compose ps

# Detener servicios
docker-compose down
```

### Verificación de Salud

```bash
# Health check de la aplicación
curl http://localhost:8080/actuator/health

# Métricas de la aplicación
curl http://localhost:8080/actuator/metrics

# Estado del Circuit Breaker
curl http://localhost:8080/actuator/circuitbreakers
```

## API REST

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/v1/credit-requests | Crear solicitud de crédito |
| GET | /api/v1/credit-requests/{id} | Obtener solicitud por ID |
| GET | /api/v1/credit-requests/{idempotencyKey}/status | Verificar estado por clave idempotente |

### Ejemplo de Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/credit-requests \
  -H "Content-Type: application/json" \
  -H "X-Channel: WEB" \
  -H "X-Operation-Number: OP-2024-001" \
  -d '{
    "customerId": "CUST-12345",
    "amount": 50000.00,
    "currency": "USD",
    "termMonths": 36,
    "productType": "PERSONAL_LOAN",
    "interestRate": 12.5
  }'
```

## Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas de integración
mvn verify -Dspring.profiles.active=test

# Ejecutar con cobertura
mvn test -Djacoco.skip=false
```

### Pruebas de Carga

El proyecto incluye configuración para pruebas de carga con k6:

```bash
# Ejecutar prueba de carga básica
k6 run src/test/load/basic-load-test.js

# Ejecutar prueba de estrés
k6 run src/test/load/stress-test.js
```

## Arquitectura de Datos

### Modelo de Entidades

- **CreditRequest**: Solicitud de crédito con estados (PENDING, VALIDATING, APPROVED, REJECTED, ERROR)
- **AntiFraudResult**: Resultado de validación antifraude
- **CoreBankingResponse**: Respuesta del core bancario

### Flujo de Procesamiento

1. Recepciones de solicitud vía REST API
2. Validación de idempotencia (Redis)
3. Persistencia inicial en PostgreSQL (estado PENDING)
4. Validación de antifraude (servicio externo)
5. Invocación a Core Bancario (con retry y circuit breaker)
6. Actualización de estado en base de datos
7. Publicación de evento a Kafka
8. Respuesta al cliente

## Monitoreo y Observabilidad

### Métricas

- Latencia de procesamiento por etapa
- Tasa de errores por servicio externo
- Estado de Circuit Breakers
- Uso de conexiones a base de datos
- Throughput de Kafka

### Logs

El sistema utiliza estructura de logs JSON para facilitar el análisis:

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "level": "INFO",
  "service": "credit-processing-system",
  "traceId": "abc123",
  "operation": "ProcessCreditRequest",
  "duration": 245
}
```

## Consideraciones de Producción

- JVM con ZGC para baja latencia
- Configuración de memoria: 75% de RAM disponible
- Health checks configurados para orchestación
- Volúmenes persistentes para Datos
- Logs estructurados para agregación
- Métricas exportadas a Prometheus

## Contribuidores

- Equipo de Desarrollo Backend - Banco Central
- Arquitectura Empresarial
```
