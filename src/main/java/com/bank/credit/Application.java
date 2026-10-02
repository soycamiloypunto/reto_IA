package com.bank.credit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Hooks;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
@EnableAsync
@ConfigurationPropertiesScan
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CircuitBreaker creditProcessingCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("creditProcessing");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    switch (event.getStateTransition()) {
                        case CLOSED_TO_OPEN:
                            log.warn("Circuit breaker opened for credit processing");
                            break;
                        case OPEN_TO_HALF_OPEN:
                            log.info("Circuit breaker half-opened for credit processing");
                            break;
                        case HALF_OPEN_TO_CLOSED:
                            log.info("Circuit breaker closed for credit processing");
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
                .onRetry(event -> log.warn("Retry attempt " + event.getNumberOfRetryAttempts() +
                        " for core banking call. Last exception: " + event.getLastThrowable().getMessage()))
                .onSuccess(event -> log.info("Retry succeeded after " +
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
