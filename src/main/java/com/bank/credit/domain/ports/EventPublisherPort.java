package com.bank.credit.domain.ports;

import com.bank.credit.domain.events.CreditRequestApprovedEvent;
import com.bank.credit.domain.events.CreditRequestRejectedEvent;
import reactor.core.publisher.Mono;

public interface EventPublisherPort {
    Mono<Void> publishApprovedEvent(CreditRequestApprovedEvent event);
    Mono<Void> publishRejectedEvent(CreditRequestRejectedEvent event);
}
