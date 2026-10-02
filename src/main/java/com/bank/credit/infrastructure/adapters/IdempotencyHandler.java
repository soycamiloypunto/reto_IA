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