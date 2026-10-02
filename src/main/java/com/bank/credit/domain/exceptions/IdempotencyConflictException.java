package com.bank.credit.domain.exceptions;

import java.time.Duration;
import java.time.Instant;

public class IdempotencyConflictException extends RuntimeException {
    private final String idempotencyKey;
    private final String channel;
    private final Instant originalRequestTime;
    private final Duration timeUntilExpiry;
    private static final long serialVersionUID = 1L;

    public IdempotencyConflictException(String idempotencyKey, String channel) {
        super(buildMessage(idempotencyKey, channel, null));
        this.idempotencyKey = idempotencyKey;
        this.channel = channel;
        this.originalRequestTime = Instant.now();
        this.timeUntilExpiry = Duration.ofHours(24);
    }

    public IdempotencyConflictException(String idempotencyKey, String channel, Instant originalRequestTime) {
        super(buildMessage(idempotencyKey, channel, originalRequestTime));
        this.idempotencyKey = idempotencyKey;
        this.channel = channel;
        this.originalRequestTime = originalRequestTime;
        long remainingMillis = Duration.between(Instant.now(), originalRequestTime.plusSeconds(86400)).toMillis();
        this.timeUntilExpiry = Duration.ofMillis(Math.max(0, remainingMillis));
    }

    private static String buildMessage(String idempotencyKey, String channel, Instant originalTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("Conflicto de idempotencia detectado. ");
        sb.append("La clave de idempotencia '[").append(idempotencyKey).append("]' ");
        sb.append("ya existe para el canal '[").append(channel).append("]'.");
        if (originalTime != null) {
            sb.append(" Solicitud original procesada el: ").append(originalTime);
        }
        return sb.toString();
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getChannel() {
        return channel;
    }

    public Instant getOriginalRequestTime() {
        return originalRequestTime;
    }

    public Duration getTimeUntilExpiry() {
        return timeUntilExpiry;
    }

    public String getErrorCode() {
        return "IDEMPOTENCY_CONFLICT";
    }

    public boolean isExpired() {
        return timeUntilExpiry.isNegative() || timeUntilExpiry.isZero();
    }

    public long getRemainingSeconds() {
        return timeUntilExpiry.getSeconds();
    }
}