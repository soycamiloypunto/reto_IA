package com.bank.credit.infrastructure.controllers;

import com.bank.credit.application.usecases.ProcessCreditRequestUseCase;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.models.CreditRequest.CreditRequestStatus;
import com.bank.credit.infrastructure.adapters.IdempotencyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/credit-requests")
@Tag(name = "Credit Requests", description = "API para gestión de solicitudes de crédito")
public class CreditRequestController {

    private static final Logger log = LoggerFactory.getLogger(CreditRequestController.class);

    private final ProcessCreditRequestUseCase processCreditRequestUseCase;
    private final IdempotencyHandler idempotencyHandler;

    public CreditRequestController(ProcessCreditRequestUseCase processCreditRequestUseCase,
                                   IdempotencyHandler idempotencyHandler) {
        this.processCreditRequestUseCase = processCreditRequestUseCase;
        this.idempotencyHandler = idempotencyHandler;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear solicitud de crédito",
            description = "Crea una nueva solicitud de crédito con validación de idempotencia. " +
                    "El sistema garantiza que múltiples invocaciones con la misma clave de idempotencia " +
                    "(operationNumber + channel) retornen el mismo resultado dentro de 24 horas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud de crédito creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "200", description = "Solicitud existente retornada (idempotencia)",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de idempotencia",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Solicitud rechazada por validación de negocio",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Mono<ResponseEntity<CreditRequestResponse>> createCreditRequest(
            @Parameter(description = "Datos de la solicitud de crédito", required = true)
            @RequestBody CreditRequestDTO request) {

        log.info("Recibida solicitud de crédito: operationNumber={}, channel={}, amount={}",
                request.operationNumber(), request.channel(), request.requestedAmount());

        return processCreditRequestUseCase.execute(
                        request.customerId(),
                        request.requestedAmount(),
                        request.termMonths(),
                        request.operationNumber(),
                        request.channel()
                )
                .map(creditRequest -> {
                    CreditRequestStatus status = creditRequest.status();
                    HttpStatus httpStatus = switch (status) {
                        case APPROVED, PENDING_APPROVAL -> HttpStatus.CREATED;
                        case REJECTED -> HttpStatus.UNPROCESSABLE_ENTITY;
                        default -> HttpStatus.INTERNAL_SERVER_ERROR;
                    };
                    return ResponseEntity.status(httpStatus)
                            .body(CreditRequestResponse.fromDomain(creditRequest));
                })
                .onErrorResume(WebExchangeBindException.class, e -> {
                    log.warn("Error de validación en solicitud: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest()
                            .body(new CreditRequestResponse(null, null, null, null, null,
                                    "VALIDATION_ERROR", e.getMessage(), null)));
                })
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Argumento inválido: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest()
                            .body(new CreditRequestResponse(null, null, null, null, null,
                                    "BAD_REQUEST", e.getMessage(), null)));
                });
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener solicitud de crédito por ID",
            description = "Recupera los detalles de una solicitud de crédito existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public Mono<ResponseEntity<CreditRequestResponse>> getCreditRequest(
            @Parameter(description = "ID de la solicitud de crédito", required = true)
            @PathVariable UUID id) {

        log.info("Consultando solicitud de crédito: id={}", id);

        return processCreditRequestUseCase.findById(id)
                .map(creditRequest -> ResponseEntity.ok(CreditRequestResponse.fromDomain(creditRequest)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/idempotency/{idempotencyKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consultar por clave de idempotencia",
            description = "Recupera una solicitud de crédito usando su clave de idempotencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = CreditRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public Mono<ResponseEntity<CreditRequestResponse>> getByIdempotencyKey(
            @Parameter(description = "Clave de idempotencia (operationNumber-channel)", required = true)
            @PathVariable String idempotencyKey) {

        log.info("Consultando por clave de idempotencia: {}", idempotencyKey);

        return processCreditRequestUseCase.findByIdempotencyKey(idempotencyKey)
                .map(creditRequest -> ResponseEntity.ok(CreditRequestResponse.fromDomain(creditRequest)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar solicitudes de crédito",
            description = "Lista todas las solicitudes de crédito con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes")
    })
    public Mono<ResponseEntity<Map<String, Object>>> listCreditRequests(
            @Parameter(description = "Número de página (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "20") int size) {

        log.info("Listando solicitudes de crédito: page={}, size={}", page, size);

        return processCreditRequestUseCase.findAll(page, size)
                .map(result -> ResponseEntity.ok(Map.of(
                        "content", result.get("content"),
                        "page", page,
                        "size", size,
                        "totalElements", result.get("totalElements"),
                        "totalPages", result.get("totalPages")
                )));
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Health check", description = "Verifica el estado del servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio disponible")
    })
    public Mono<ResponseEntity<Map<String, String>>> health() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "credit-processing-system",
                "timestamp", java.time.Instant.now().toString()
        )));
    }

    public record CreditRequestDTO(
            String customerId,
            BigDecimal requestedAmount,
            Integer termMonths,
            String operationNumber,
            String channel,
            BigDecimal interestRate,
            String purpose
    ) {}

    public record CreditRequestResponse(
            UUID id,
            String customerId,
            BigDecimal requestedAmount,
            Integer termMonths,
            String status,
            String idempotencyKey,
            String message,
            LocalDate createdAt
    ) {
        public static CreditRequestResponse fromDomain(CreditRequest creditRequest) {
            return new CreditRequestResponse(
                    creditRequest.id(),
                    creditRequest.customerId(),
                    creditRequest.requestedAmount(),
                    creditRequest.termMonths(),
                    creditRequest.status().name(),
                    creditRequest.idempotencyKey(),
                    null,
                    creditRequest.createdAt()
            );
        }
    }

    public record ErrorResponse(
            String error,
            String message,
            String details
    ) {}
}