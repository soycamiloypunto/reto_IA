package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

public interface AntiFraudPort {
    Mono<AntiFraudResult> validate(CreditRequest creditRequest);

    record AntiFraudResult(
        boolean approved,
        String riskLevel,
        String reason
    ) {}
}
