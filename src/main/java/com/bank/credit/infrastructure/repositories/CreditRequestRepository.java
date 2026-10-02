package com.bank.credit.infrastructure.repositories;

import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequest.CreditRequestStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Repositorio reactivo para la persistencia de solicitudes de crédito usando R2DBC.
 * Proporciona operaciones asíncronas sobre la tabla de solicitudes de crédito
 * sin bloquear el hilo de ejecución.
 */
@Repository
public interface CreditRequestRepository extends ReactiveCrudRepository<CreditRequest, UUID> {

    /**
     * Busca una solicitud por su clave de idempotencia.
     * La clave de idempotencia se compone del número de operación + canal.
     * @param idempotencyKey la clave de idempotencia
     * @return Mono con la solicitud encontrada o vacío
     */
    Mono<CreditRequest> findByIdempotencyKey(String idempotencyKey);

    /**
     * Busca solicitudes por estado.
     * @param status el estado de las solicitudes a buscar
     * @return Flux con las solicitudes que coinciden
     */
    Flux<CreditRequest> findByStatus(CreditRequestStatus status);

    /**
     * Busca solicitudes por identificador de cliente.
     * @param customerId el identificador del cliente
     * @return Flux con las solicitudes del cliente
     */
    Flux<CreditRequest> findByCustomerId(String customerId);

    /**
     * Actualiza el estado de una solicitud y la razón de rechazo si aplica.
     * @param id el identificador de la solicitud
     * @param status el nuevo estado
     * @param rejectionReason la razón de rechazo (opcional)
     * @param processedAt la fecha de procesamiento
     * @return Mono con el número de filas afectadas
     */
    @Modifying
    @Query("UPDATE credit_requests SET status = :status, rejection_reason = :rejectionReason, " +
           "processed_at = :processedAt, updated_at = :updatedAt WHERE id = :id")
    Mono<Integer> updateStatus(UUID id, CreditRequestStatus status, String rejectionReason,
                                Instant processedAt, Instant updatedAt);

    /**
     * Busca solicitudes pendientes de procesamiento.
     * @return Flux con las solicitudes pendientes
     */
    @Query("SELECT * FROM credit_requests WHERE status = 'PENDING' AND created_at > :since " +
           "ORDER BY created_at ASC LIMIT :limit")
    Flux<CreditRequest> findPendingRequests(Instant since, int limit);

    /**
     * Busca solicitudes por rango de monto y estado.
     * @param minAmount monto mínimo
     * @param maxAmount monto máximo
     * @param status estado de las solicitudes
     * @return Flux con las solicitudes que coinciden
     */
    Flux<CreditRequest> findByAmountBetweenAndStatus(BigDecimal minAmount,
                                                       BigDecimal maxAmount,
                                                       CreditRequestStatus status);

    /**
     * Cuenta el número de solicitudes por estado.
     * @param status el estado a contar
     * @return Mono con el conteo
     */
    Mono<Long> countByStatus(CreditRequestStatus status);

    /**
     * Verifica si existe una solicitud con la clave de idempotencia dada.
     * @param idempotencyKey la clave de idempotencia
     * @return Mono con true si existe
     */
    Mono<Boolean> existsByIdempotencyKey(String idempotencyKey);
}