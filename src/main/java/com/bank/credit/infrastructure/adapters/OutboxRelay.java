package com.bank.credit.infrastructure.adapters;

import com.bank.credit.domain.models.OutboxEvent;
import com.bank.credit.infrastructure.repositories.OutboxEventRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.LocalDateTime;

@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);
    private final OutboxEventRepository repository;
    private final KafkaSender<String, Object> kafkaSender;

    public OutboxRelay(OutboxEventRepository repository, KafkaSender<String, Object> kafkaSender) {
        this.repository = repository;
        this.kafkaSender = kafkaSender;
    }

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        repository.findByStatus("PENDING")
            .flatMap(this::publishEvent)
            .subscribe();
    }

    @Transactional
    public Mono<Void> publishEvent(OutboxEvent event) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("credit-events", event.aggregateId(), event.payload());
        SenderRecord<String, Object, String> senderRecord = SenderRecord.create(record, event.id().toString());
        
        return kafkaSender.send(Mono.just(senderRecord))
            .next()
            .flatMap(res -> {
                OutboxEvent updated = new OutboxEvent(event.id(), event.aggregateType(), event.aggregateId(), event.type(), event.payload(), "PROCESSED", event.createdAt());
                return repository.save(updated);
            }).then();
    }
}
