package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

public interface IdempotencyPort {
    Mono<CreditRequest> handleIdempotentOperation(String operationNumber, String channel, Mono<CreditRequest> operation);
    Mono<Boolean> validateIdempotencyKey(String operationNumber, String channel);
}
