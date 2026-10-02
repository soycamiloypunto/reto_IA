package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.CreditRequestPort;
import org.redisson.api.RedissonReactiveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class IdempotencyHandler implements com.bank.credit.domain.ports.IdempotencyPort {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyHandler.class);
    private static final Duration IDEMPOTENCY_TTL = Duration.ofHours(24);
    private static final String IDEMPOTENCY_MAP_NAME = "credit:idempotency:";

    private final RedissonReactiveClient redissonClient;
    private final CreditRequestPort creditRequestPort;

    public IdempotencyHandler(RedissonReactiveClient redissonClient, CreditRequestPort creditRequestPort) {
        this.redissonClient = redissonClient;
        this.creditRequestPort = creditRequestPort;
    }

    @Override
    public Mono<CreditRequest> handleIdempotentOperation(
            String operationNumber, 
            String channel, 
            Mono<CreditRequest> operation) {
        
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        log.info("Procesando operación idempotente con clave: {}", idempotencyKey);
        
        return redissonClient.getBucket(IDEMPOTENCY_MAP_NAME + idempotencyKey)
                .setIfAbsent("PROCESSING", IDEMPOTENCY_TTL)
                .flatMap(isNew -> {
                    if (Boolean.TRUE.equals(isNew)) {
                        return operation
                            .map(result -> enrichWithIdempotencyKey(result, idempotencyKey))
                            .flatMap(result -> creditRequestPort.save(result))
                            .onErrorResume(Exception.class, e -> {
                                log.warn("Conflicto de clave duplicada en BD para clave: {}", idempotencyKey);
                                return creditRequestPort.findByIdempotencyKey(idempotencyKey);
                            });
                    } else {
                        log.info("Operación en progreso o completada, buscando resultado para clave: {}", idempotencyKey);
                        return creditRequestPort.findByIdempotencyKey(idempotencyKey);
                    }
                });
    }

    @Override
    public Mono<Boolean> validateIdempotencyKey(String operationNumber, String channel) {
        String idempotencyKey = generateIdempotencyKey(operationNumber, channel);
        return redissonClient.getBucket(IDEMPOTENCY_MAP_NAME + idempotencyKey).isExists();
    }

    private String generateIdempotencyKey(String operationNumber, String channel) {
        return channel + ":" + operationNumber;
    }

    private CreditRequest enrichWithIdempotencyKey(CreditRequest request, String idempotencyKey) {
        return new CreditRequest(
                request.id(), request.customerId(), request.requestedAmount(), request.termMonths(),
                request.interestRate(), request.currency(), request.channel(), request.operationNumber(),
                idempotencyKey, request.status(), request.createdAt(), request.updatedAt(),
                request.rejectionReason(), request.applicantId(), request.applicantName(),
                request.applicantEmail(), request.applicantPhone()
        );
    }
}
