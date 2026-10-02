package com.bank.credit.domain.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;
@Table("outbox_events")
public record OutboxEvent(
    @Id UUID id,
    String aggregateType,
    String aggregateId,
    String type,
    String payload,
    String status,
    LocalDateTime createdAt
) {}
