package com.bank.credit.domain.exceptions;

import java.time.Instant;
import java.util.UUID;

public class CreditRequestNotFoundException extends RuntimeException {
    private final UUID requestId;
    private final Instant timestamp;
    private final String searchContext;
    private static final long serialVersionUID = 1L;

    public CreditRequestNotFoundException(UUID requestId) {
        super(buildMessage(requestId, null));
        this.requestId = requestId;
        this.timestamp = Instant.now();
        this.searchContext = "ID";
    }

    public CreditRequestNotFoundException(String idempotencyKey) {
        super(buildMessage(null, idempotencyKey));
        this.requestId = null;
        this.timestamp = Instant.now();
        this.searchContext = "IDEMPOTENCY_KEY";
    }

    public CreditRequestNotFoundException(UUID requestId, String idempotencyKey) {
        super(buildMessage(requestId, idempotencyKey));
        this.requestId = requestId;
        this.timestamp = Instant.now();
        this.searchContext = requestId != null ? "ID" : "IDEMPOTENCY_KEY";
    }

    private static String buildMessage(UUID requestId, String idempotencyKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("Solicitud de crédito no encontrada. ");
        if (requestId != null) {
            sb.append("ID: ").append(requestId);
        }
        if (idempotencyKey != null) {
            if (requestId != null) sb.append(", ");
            sb.append("Clave de idempotencia: ").append(idempotencyKey);
        }
        return sb.toString();
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSearchContext() {
        return searchContext;
    }

    public String getErrorCode() {
        return "CREDIT_REQUEST_NOT_FOUND";
    }

    public String getDetails() {
        return String.format("No se encontró la solicitud de crédito con %s: %s", 
            searchContext, 
            requestId != null ? requestId.toString() : getMessage().split("Clave de idempotencia: ")[1]);
    }

    public boolean hasRequestId() {
        return requestId != null;
    }

    public boolean hasIdempotencyKey() {
        return searchContext.equals("IDEMPOTENCY_KEY");
    }
}