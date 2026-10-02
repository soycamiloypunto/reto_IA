package com.bank.credit.domain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("credit_requests")
public record CreditRequest(
    @Id UUID id,
    String customerId,
    BigDecimal requestedAmount,
    Integer termMonths,
    BigDecimal interestRate,
    String currency,
    String channel,
    String operationNumber,
    String idempotencyKey,
    CreditRequestStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String rejectionReason,
    String applicantId,
    String applicantName,
    String applicantEmail,
    String applicantPhone
) {}
