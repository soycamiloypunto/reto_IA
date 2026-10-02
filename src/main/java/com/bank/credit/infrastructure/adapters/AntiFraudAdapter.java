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