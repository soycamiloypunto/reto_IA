package com.bank.credit.domain.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditRequestApprovedEvent(
    String id,
    String customerId,
    BigDecimal requestedAmount,
    String coreReferenceId,
    LocalDateTime approvedAt
) {}
