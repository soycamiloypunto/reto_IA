package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
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

@Component
public class AntiFraudAdapter implements AntiFraudPort {

    private static final Logger log = LoggerFactory.getLogger(AntiFraudAdapter.class);

    @Override
    @CircuitBreaker(name = "antiFraud")
    @Retry(name = "antiFraud")
    @TimeLimiter(name = "antiFraud")
    @Bulkhead(name = "antiFraud")
    public Mono<AntiFraudResult> validate(CreditRequest request) {
        log.info("Validando fraude para el cliente: {}", request.customerId());
        return Mono.delay(Duration.ofMillis(100))
                   .map(l -> performValidation(request))
                   .doOnSuccess(result -> log.info("Validación de fraude exitosa: approved={}", result.approved()));
    }

    private AntiFraudResult performValidation(CreditRequest request) {
        String customerId = request.customerId();
        BigDecimal amount = request.requestedAmount();
        
        if (customerId == null || customerId.isBlank()) {
            return new AntiFraudResult(false, "HIGH", "Invalid customer ID");
        }
        
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            return new AntiFraudResult(false, "HIGH", "Amount exceeds maximum allowed limit");
        }
        
        if (customerId.startsWith("FRAUD-")) {
            return new AntiFraudResult(false, "HIGH", "Customer flagged for fraud");
        }
        
        return new AntiFraudResult(true, "LOW", "Approved");
    }
}
