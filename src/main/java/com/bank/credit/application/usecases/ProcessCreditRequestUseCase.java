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