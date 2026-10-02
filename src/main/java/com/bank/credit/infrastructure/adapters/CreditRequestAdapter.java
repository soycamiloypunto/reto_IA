package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.OutboxEvent;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.infrastructure.repositories.CreditRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreditRequestAdapter implements CreditRequestPort {

    private final CreditRequestRepository repository;
    private final R2dbcEntityTemplate template;
    private final ObjectMapper objectMapper;

    public CreditRequestAdapter(CreditRequestRepository repository, R2dbcEntityTemplate template, ObjectMapper objectMapper) {
        this.repository = repository;
        this.template = template;
        this.objectMapper = objectMapper;
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
    @Transactional
    public Mono<CreditRequest> updateStatus(UUID id, CreditRequestStatus status, String rejectionReason) {
        return repository.findById(id)
                .flatMap(req -> {
                    CreditRequest updated = new CreditRequest(
                            req.id(), req.customerId(), req.requestedAmount(), req.termMonths(),
                            req.interestRate(), req.currency(), req.channel(), req.operationNumber(),
                            req.idempotencyKey(), status, req.createdAt(),
                            LocalDateTime.now(), rejectionReason,
                            req.applicantId(), req.applicantName(), req.applicantEmail(), req.applicantPhone()
                    );
                    
                    String eventType = "CreditRequest" + status.name();
                    String payload = "";
                    try {
                        payload = objectMapper.writeValueAsString(updated);
                    } catch (JsonProcessingException e) {}

                    OutboxEvent event = new OutboxEvent(
                        UUID.randomUUID(), "CreditRequest", updated.id().toString(), 
                        eventType, payload, "PENDING", LocalDateTime.now()
                    );
                    
                    return template.update(updated).then(template.insert(event)).thenReturn(updated);
                });
    }
}
