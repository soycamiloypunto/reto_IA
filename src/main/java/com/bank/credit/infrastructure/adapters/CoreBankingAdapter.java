package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.CoreBankingPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Component
public class CoreBankingAdapter implements CoreBankingPort {

    private static final Logger log = LoggerFactory.getLogger(CoreBankingAdapter.class);

    @Override
    @CircuitBreaker(name = "coreBanking")
    @Retry(name = "coreBanking")
    @TimeLimiter(name = "coreBanking")
    @Bulkhead(name = "coreBanking")
    public Mono<CoreBankingResult> submitCreditRequest(CreditRequest creditRequest) {
        log.info("Registrando solicitud de crédito en core bancario: idempotencyKey={}, amount={}",
                creditRequest.idempotencyKey(), creditRequest.requestedAmount());

        return Mono.delay(Duration.ofMillis(200))
                   .map(l -> performCoreBankingRegistration(creditRequest))
                   .doOnSuccess(result -> log.info("Registro en core bancario exitoso: refId={}", result.referenceId()));
    }

    @Override
    public Mono<CoreBankingStatus> queryStatus(String coreReferenceId) {
        return Mono.just(CoreBankingStatus.APPROVED);
    }

    @Override
    public Mono<CoreBankingResult> cancelCreditRequest(String coreReferenceId, String reason) {
        return Mono.just(CoreBankingResult.success(coreReferenceId));
    }

    private CoreBankingResult performCoreBankingRegistration(CreditRequest creditRequest) {
        String customerId = creditRequest.customerId();
        BigDecimal amount = creditRequest.requestedAmount();

        if (customerId == null || customerId.isBlank()) {
            return CoreBankingResult.failure("Invalid customer ID", CoreBankingErrorCode.INVALID_CUSTOMER);
        }

        String referenceId = "REF-" + UUID.randomUUID().toString().substring(0, 8);
        return CoreBankingResult.success(referenceId);
    }
}
