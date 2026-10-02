package com.bank.credit.domain.exceptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvalidCreditRequestException extends RuntimeException {
    private final List<ValidationError> validationErrors;
    private static final long serialVersionUID = 1L;

    public InvalidCreditRequestException(String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
        addError("GENERAL", message);
    }

    public InvalidCreditRequestException(String field, String message) {
        super(buildMessage(field, message));
        this.validationErrors = new ArrayList<>();
        addError(field, message);
    }

    public InvalidCreditRequestException(List<ValidationError> errors) {
        super(buildErrorListMessage(errors));
        this.validationErrors = new ArrayList<>(errors);
    }

    private static String buildMessage(String field, String message) {
        return String.format("Error de validación en '%s': %s", field, message);
    }

    private static String buildErrorListMessage(List<ValidationError> errors) {
        if (errors.isEmpty()) {
            return "La solicitud de crédito contiene errores de validación";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("La solicitud de crédito contiene ");
        sb.append(errors.size());
        sb.append(" error(es) de validación: ");
        for (int i = 0; i < errors.size(); i++) {
            sb.append(errors.get(i).getField()).append(" - ").append(errors.get(i).getMessage());
            if (i < errors.size() - 1) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }

    public void addError(String field, String message) {
        this.validationErrors.add(new ValidationError(field, message));
    }

    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public String getErrorCode() {
        return "INVALID_CREDIT_REQUEST";
    }

    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }

    public int getErrorCount() {
        return validationErrors.size();
    }

    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}