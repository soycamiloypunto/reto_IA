package com.bank.credit.domain.models;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio que representa una solicitud de crédito.
 * Contiene las reglas de negocio y validaciones asociadas.
 */
public record CreditRequest(
    @NotNull
    UUID id,

    @NotNull
    @Size(min = 1, max = 50)
    String customerId,

    @NotNull
    @PositiveOrZero
    BigDecimal amount,

    @NotNull
    @Min(1)
    @Max(360)
    Integer termMonths,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0")
    BigDecimal interestRate,

    @NotNull
    @Size(min = 3, max = 3)
    String currency,

    @NotNull
    @Size(min = 1, max = 100)
    String channel,

    @NotNull
    @Size(min = 1, max = 100)
    String operationNumber,

    @NotNull
    CreditRequestStatus status,

    @NotNull
    LocalDateTime createdAt,

    LocalDateTime updatedAt,

    @Size(max = 500)
    String rejectionReason
) {
    /**
     * Constructor con validaciones de negocio.
     * @throws IllegalArgumentException si alguna regla de negocio no se cumple.
     */
    public CreditRequest {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        if (termMonths < 1 || termMonths > 360) {
            throw new IllegalArgumentException("El plazo debe estar entre 1 y 360 meses");
        }
        if (interestRate.compareTo(BigDecimal.ZERO) <= 0 || interestRate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("La tasa de interés debe estar entre 0 y 100");
        }
        if (!"COP".equals(currency) && !"USD".equals(currency)) {
            throw new IllegalArgumentException("Moneda no soportada");
        }
        if (status == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
    }

    /**
     * Calcula la cuota mensual para esta solicitud de crédito.
     * @return cuota mensual calculada.
     */
    public BigDecimal calculateMonthlyPayment() {
        BigDecimal monthlyRate = interestRate.divide(new BigDecimal("12"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal numerator = amount.multiply(monthlyRate).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyRate.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP))
                .pow(-termMonths));
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Genera la clave de idempotencia para esta solicitud.
     * @return clave de idempotencia en formato "operationNumber:channel".
     */
    public String generateIdempotencyKey() {
        return operationNumber + ":" + channel;
    }

    /**
     * Verifica si la solicitud cumple con las condiciones para ser aprobada.
     * @return true si la solicitud puede ser aprobada.
     */
    public boolean isEligibleForApproval() {
        return status == CreditRequestStatus.PENDING && 
               amount.compareTo(new BigDecimal("10000000")) <= 0 && // Límite de 10 millones
               termMonths <= 60; // Plazo máximo de 5 años para aprobación automática
    }

    /**
     * Enum que representa los posibles estados de una solicitud de crédito.
     */
    public enum CreditRequestStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        FAILED
    }
}