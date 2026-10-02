package com.bank.credit.domain.ports;

import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para la integración con el core bancario.
 * Define el contrato que la infraestructura debe implementar para comunicar
 * el sistema de crédito con los servicios del core bancario del banco.
 */
public interface CoreBankingPort {

    /**
     * Envía una solicitud de crédito aprobada al core bancario para su procesamiento.
     * @param creditRequest la solicitud de crédito aprobada con toda la información necesaria
     * @return Mono con el resultado del procesamiento del core bancario
     */
    Mono<CoreBankingResult> submitCreditRequest(CreditRequest creditRequest);

    /**
     * Consulta el estado de una solicitud previamente enviada al core bancario.
     * @param coreReferenceId el identificador de referencia del core bancario
     * @return Mono con el estado actual de la operación en el core
     */
    Mono<CoreBankingStatus> queryStatus(String coreReferenceId);

    /**
     * Cancela una operación de crédito previamente enviada al core bancario.
     * @param coreReferenceId el identificador de referencia del core bancario
     * @param reason motivo de la cancelación
     * @return Mono con el resultado de la operación de cancelación
     */
    Mono<CoreBankingResult> cancelCreditRequest(String coreReferenceId, String reason);

    /**
     * Resultado retornado por el core bancario tras procesar una solicitud.
     */
    record CoreBankingResult(
        boolean success,
        String referenceId,
        String message,
        CoreBankingErrorCode errorCode
    ) {
        public static CoreBankingResult success(String referenceId) {
            return new CoreBankingResult(true, referenceId, "Operation completed successfully", null);
        }

        public static CoreBankingResult failure(String message, CoreBankingErrorCode errorCode) {
            return new CoreBankingResult(false, null, message, errorCode);
        }
    }

    /**
     * Estado de una operación en el core bancario.
     */
    enum CoreBankingStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        CANCELLED,
        FAILED
    }

    /**
     * Códigos de error del core bancario.
     */
    enum CoreBankingErrorCode {
        INVALID_CUSTOMER,
        INSUFFICIENT_CREDIT,
        DUPLICATE_REFERENCE,
        SYSTEM_ERROR,
        TIMEOUT,
        VALIDATION_ERROR
    }
}