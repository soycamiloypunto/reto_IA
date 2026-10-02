package com.bank.credit.domain.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AntiFraudValidationException extends RuntimeException {
    private final UUID requestId;
    private final Instant validationTime;
    private final List<RiskFactor> riskFactors;
    private final FraudRiskLevel riskLevel;
    private static final long serialVersionUID = 1L;

    public AntiFraudValidationException(UUID requestId, String message) {
        super(buildMessage(requestId, message, null, FraudRiskLevel.UNKNOWN));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = new ArrayList<>();
        this.riskLevel = FraudRiskLevel.UNKNOWN;
    }

    public AntiFraudValidationException(UUID requestId, List<RiskFactor> riskFactors, FraudRiskLevel riskLevel) {
        super(buildMessage(requestId, null, riskFactors, riskLevel));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = new ArrayList<>(riskFactors);
        this.riskLevel = riskLevel;
    }

    public AntiFraudValidationException(UUID requestId, String message, List<RiskFactor> riskFactors, FraudRiskLevel riskLevel) {
        super(buildMessage(requestId, message, riskFactors, riskLevel));
        this.requestId = requestId;
        this.validationTime = Instant.now();
        this.riskFactors = riskFactors != null ? new ArrayList<>(riskFactors) : new ArrayList<>();
        this.riskLevel = riskLevel;
    }

    private static String buildMessage(UUID requestId, String message, List<RiskFactor> factors, FraudRiskLevel level) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validación de antifraude fallida para la solicitud: ").append(requestId);
        if (message != null) {
            sb.append(". Razón: ").append(message);
        }
        if (level != null && level != FraudRiskLevel.UNKNOWN) {
            sb.append(". Nivel de riesgo: ").append(level.name());
        }
        if (factors != null && !factors.isEmpty()) {
            sb.append(". Factores de riesgo detectados: ").append(factors.size());
        }
        return sb.toString();
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Instant getValidationTime() {
        return validationTime;
    }

    public List<RiskFactor> getRiskFactors() {
        return Collections.unmodifiableList(riskFactors);
    }

    public FraudRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getErrorCode() {
        return "ANTIFRAUD_VALIDATION_FAILED";
    }

    public boolean hasRiskFactors() {
        return !riskFactors.isEmpty();
    }

    public boolean isHighRisk() {
        return riskLevel == FraudRiskLevel.HIGH || riskLevel == FraudRiskLevel.CRITICAL;
    }

    public static class RiskFactor {
        private final String factor;
        private final String description;
        private final double score;

        public RiskFactor(String factor, String description, double score) {
            this.factor = factor;
            this.description = description;
            this.score = score;
        }

        public String getFactor() {
            return factor;
        }

        public String getDescription() {
            return description;
        }

        public double getScore() {
            return score;
        }
    }

    public enum FraudRiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL,
        UNKNOWN
    }
}