package com.bank.credit.domain.ports;


import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Puerto de dominio para operaciones relacionadas con solicitudes de crédito.
 * Define las operaciones que la capa de aplicación puede realizar sobre el dominio.
 */
public interface CreditRequestPort {
    /**
     * Guarda una solicitud de crédito en el sistema.
     * @param creditRequest la solicitud a guardar.
     * @return Mono con la solicitud guardada.
     */
    Mono<CreditRequest> save(CreditRequest creditRequest);

    /**
     * Busca una solicitud de crédito por su ID.
     * @param id el ID de la solicitud.
     * @return Mono con la solicitud encontrada, o Mono.empty() si no existe.
     */
    Mono<CreditRequest> findById(UUID id);

    /**
     * Busca una solicitud de crédito por su clave de idempotencia.
     * @param idempotencyKey la clave de idempotencia.
     * @return Mono con la solicitud encontrada, o Mono.empty() si no existe.
     */
    Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey);

    /**
     * Actualiza el estado de una solicitud de crédito.
     * @param id el ID de la solicitud.
     * @param status el nuevo estado.
     * @param rejectionReason la razón de rechazo (opcional).
     * @return Mono con la solicitud actualizada.
     */
    Mono<CreditRequest> updateStatus(UUID id, CreditRequestStatus status, String rejectionReason);
}