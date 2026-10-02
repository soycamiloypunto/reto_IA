package com.bank.credit.domain.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditRequestRejectedEvent(
    String id,
    String customerId,
    BigDecimal requestedAmount,
    String reason,
    LocalDateTime rejectedAt
) {}
