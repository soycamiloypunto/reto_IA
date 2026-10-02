package com.bank.credit.infrastructure.repositories;
import com.bank.credit.domain.models.OutboxEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import java.util.UUID;
public interface OutboxEventRepository extends ReactiveCrudRepository<OutboxEvent, UUID> {
    Flux<OutboxEvent> findByStatus(String status);
}
