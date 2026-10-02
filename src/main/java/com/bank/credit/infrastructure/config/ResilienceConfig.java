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