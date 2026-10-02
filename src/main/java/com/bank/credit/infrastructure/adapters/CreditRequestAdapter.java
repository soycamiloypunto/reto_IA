package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.infrastructure.repositories.CreditRequestRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreditRequestAdapter implements CreditRequestPort {

    private final CreditRequestRepository repository;
    private final R2dbcEntityTemplate template;

    public CreditRequestAdapter(CreditRequestRepository repository, R2dbcEntityTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public Mono<CreditRequest> save(CreditRequest creditRequest) {
        return template.insert(creditRequest);
    }

    @Override
    public Mono<CreditRequest> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey);
    }

    @Override
    public Mono<CreditRequest> updateStatus(UUID id, CreditRequestStatus status, String rejectionReason) {
        return repository.findById(id)
                .flatMap(req -> {
                    CreditRequest updated = new CreditRequest(
                            req.id(), req.customerId(), req.requestedAmount(), req.termMonths(),
                            req.interestRate(), req.currency(), req.channel(), req.operationNumber(),
                            req.idempotencyKey(), status, req.createdAt(),
                            java.time.LocalDateTime.now(), rejectionReason,
                            req.applicantId(), req.applicantName(), req.applicantEmail(), req.applicantPhone()
                    );
                    return template.update(updated);
                });
    }
}
