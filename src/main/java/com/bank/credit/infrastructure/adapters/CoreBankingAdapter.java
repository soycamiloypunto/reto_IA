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