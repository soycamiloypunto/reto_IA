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