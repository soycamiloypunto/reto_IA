package com.bank.credit.application.usecases;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.ports.AntiFraudPort;
import com.bank.credit.domain.ports.AntiFraudPort.AntiFraudResult;
import com.bank.credit.domain.ports.CoreBankingPort;
import com.bank.credit.domain.ports.CoreBankingPort.CoreBankingResult;
import com.bank.credit.domain.ports.CoreBankingPort.CoreBankingErrorCode;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.domain.ports.IdempotencyPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class ProcessCreditRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessCreditRequestUseCase.class);

    private final CreditRequestPort creditRequestPort;
    private final AntiFraudPort antiFraudPort;
    private final CoreBankingPort coreBankingPort;
    private final IdempotencyPort idempotencyHandler;

    public ProcessCreditRequestUseCase(
            CreditRequestPort creditRequestPort,
            AntiFraudPort antiFraudPort,
            CoreBankingPort coreBankingPort,
            IdempotencyPort idempotencyHandler) {
        this.creditRequestPort = creditRequestPort;
        this.antiFraudPort = antiFraudPort;
        this.coreBankingPort = coreBankingPort;
        this.idempotencyHandler = idempotencyHandler;
    }

    public Mono<CreditProcessingResult> execute(CreditRequest creditRequest) {
        return process(creditRequest);
    }

    public Mono<CreditRequest> findById(UUID id) {
        return creditRequestPort.findById(id);
    }

    public Mono<CreditRequest> findByIdempotencyKey(String key) {
        return creditRequestPort.findByIdempotencyKey(key);
    }

    public Mono<java.util.Map<String, Object>> findAll(int page, int size) {
        return Mono.just(java.util.Map.of("content", java.util.List.of(), "totalElements", 0, "totalPages", 0));
    }

    public Mono<CreditProcessingResult> process(CreditRequest creditRequest) {
        String idempotencyKey = creditRequest.channel() + ":" + creditRequest.operationNumber();
        
        return idempotencyHandler.handleIdempotentOperation(
            creditRequest.operationNumber(),
            creditRequest.channel(),
            Mono.just(creditRequest)
        ).flatMap(request -> {
            if (request.id() != null && !request.id().equals(creditRequest.id())) {
                log.info("Solicitud duplicada detectada para clave: {}", idempotencyKey);
                return Mono.just(CreditProcessingResult.duplicate(request, idempotencyKey));
            }
            return processNewRequest(request, idempotencyKey);
        });
    }

    private Mono<CreditProcessingResult> processNewRequest(CreditRequest creditRequest, String idempotencyKey) {
        return Mono.zip(
                Mono.just(creditRequest),
                validateAntiFraud(creditRequest)
            )
            .flatMap(tuple -> {
                String validationResult = tuple.getT2();
                if ("TIMEOUT".equals(validationResult)) {
                    return creditRequestPort.updateStatus(creditRequest.id(), CreditRequestStatus.PENDING_REVIEW, "AntiFraud Timeout")
                        .thenReturn(CreditProcessingResult.pending(creditRequest, idempotencyKey, "AntiFraud timeout, pending review"));
                }
                return updateStatusAndNotify(tuple.getT1(), idempotencyKey);
            })
            .onErrorResume(error -> handleProcessingError(creditRequest, idempotencyKey, error));
    }

    private Mono<String> validateAntiFraud(CreditRequest request) {
        log.info("Ejecutando validación antifraude para cliente: {}", request.customerId());
        return antiFraudPort.validate(request)
            .map(result -> {
                if (result.approved()) {
                    return "APPROVED";
                }
                throw new AntiFraudRejectionException(result.reason());
            })
            .onErrorResume(TimeoutException.class, e -> {
                log.warn("Timeout en antifraude para cliente: {}", request.customerId());
                return Mono.just("TIMEOUT");
            });
    }

    private Mono<CreditProcessingResult> updateStatusAndNotify(CreditRequest request, String idempotencyKey) {
        return submitToCoreBanking(request)
            .flatMap(coreResult -> {
                if (coreResult.success()) {
                    return creditRequestPort.updateStatus(request.id(), CreditRequestStatus.APPROVED, null)
                        .thenReturn(CreditProcessingResult.success(request, idempotencyKey, coreResult.referenceId()));
                } else if (coreResult.errorCode() == CoreBankingErrorCode.TIMEOUT) {
                    return creditRequestPort.updateStatus(request.id(), CreditRequestStatus.PENDING_REVIEW, "Core Banking Timeout")
                        .thenReturn(CreditProcessingResult.pending(request, idempotencyKey, "Timeout with core banking, pending review"));
                } else {
                    return creditRequestPort.updateStatus(request.id(), CreditRequestStatus.FAILED, coreResult.message())
                        .thenReturn(CreditProcessingResult.failure(request, idempotencyKey, coreResult.message()));
                }
            });
    }

    private Mono<CoreBankingResult> submitToCoreBanking(CreditRequest request) {
        return coreBankingPort.submitCreditRequest(request)
            .doOnSuccess(result -> log.info("Core bancario procesó solicitud: ref={}, success={}",
                result.referenceId(), result.success()))
            .doOnError(error -> log.error("Error en comunicación con core bancario: {}", error.getMessage()))
            .onErrorResume(error -> {
                if (error instanceof java.util.concurrent.TimeoutException) {
                    log.warn("Timeout del core bancario, degradando a pending");
                    return Mono.just(CoreBankingResult.failure("Timeout", CoreBankingErrorCode.TIMEOUT));
                }
                return Mono.just(CoreBankingResult.failure("System Error", CoreBankingErrorCode.SYSTEM_ERROR));
            });
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

    public record CreditProcessingResult(
        ProcessingStatus status,
        UUID requestId,
        String idempotencyKey,
        String coreBankingReference,
        String message,
        Instant processedAt
    ) {
        public static CreditProcessingResult success(CreditRequest request, String idempotencyKey, String coreRef) {
            return new CreditProcessingResult(ProcessingStatus.SUCCESS, request.id(), idempotencyKey, coreRef, "Credit request processed successfully", Instant.now());
        }
        public static CreditProcessingResult failure(CreditRequest request, String idempotencyKey, String reason) {
            return new CreditProcessingResult(ProcessingStatus.FAILED, request.id(), idempotencyKey, null, reason, Instant.now());
        }
        public static CreditProcessingResult duplicate(CreditRequest existing, String idempotencyKey) {
            return new CreditProcessingResult(ProcessingStatus.DUPLICATE, existing.id(), idempotencyKey, null, "Previous request found with same idempotency key", Instant.now());
        }
        public static CreditProcessingResult pending(CreditRequest request, String idempotencyKey, String message) {
            return new CreditProcessingResult(ProcessingStatus.PENDING, request.id(), idempotencyKey, null, message, Instant.now());
        }
    }

    public enum ProcessingStatus {
        SUCCESS,
        FAILED,
        DUPLICATE,
        PENDING
    }

    public static class AntiFraudRejectionException extends RuntimeException {
        public AntiFraudRejectionException(String reason) {
            super("AntiFraud rejection: " + reason);
        }
    }
}
