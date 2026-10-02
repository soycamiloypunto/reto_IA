package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para la validación antifraude.
 * Define la interfaz que debe implementar cualquier adaptador de antifraude.
 */
public interface AntiFraudPort {
    /**
     * Valida una solicitud de crédito contra el motor antifraude.
     * @param creditRequest la solicitud a validar.
     * @return Mono con el resultado de la validación antifraude.
     */
    Mono<AntiFraudResult> validate(CreditRequest creditRequest);

    /**
     * Resultado de la validación antifraude.
     */
    record AntiFraudResult(
        boolean isFraudulent,
        String riskLevel,
        String rejectionReason
    ) {
        /**
         * Verifica si la solicitud debe ser rechazada por fraude.
         * @return true si la solicitud debe ser rechazada.
         */
        public boolean shouldReject() {
            return isFraudulent || "HIGH".equals(riskLevel);
        }
    }
}