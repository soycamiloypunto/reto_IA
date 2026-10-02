package com.bank.credit.infrastructure.exceptions;

import com.bank.credit.domain.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CreditRequestNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCreditRequestNotFound(CreditRequestNotFoundException ex) {
        log.warn("Solicitud de crédito no encontrada: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getErrorCode(),
            ex.getMessage(),
            Instant.now(),
            buildMetadata(ex)
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(IdempotencyConflictException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIdempotencyConflict(IdempotencyConflictException ex) {
        log.warn("Conflicto de idempotencia: {}", ex.getMessage());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("idempotencyKey", ex.getIdempotencyKey());
        metadata.put("channel", ex.getChannel());
        metadata.put("originalRequestTime", ex.getOriginalRequestTime().toString());
        metadata.put("remainingSeconds", ex.getRemainingSeconds());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getErrorCode(),
            "La solicitud ya fue procesada. Utilice la respuesta original.",
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    @ExceptionHandler(InvalidCreditRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidCreditRequest(InvalidCreditRequestException ex) {
        log.warn("Solicitud de crédito inválida: {}", ex.getMessage());
        
        Map<String, Object> metadata = new HashMap<>();
        if (ex.hasErrors()) {
            metadata.put("validationErrors", ex.getValidationErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getMessage()))
                .collect(Collectors.toList()));
            metadata.put("errorCount", ex.getErrorCount());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getErrorCode(),
            ex.getMessage(),
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(AntiFraudValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAntiFraudValidation(AntiFraudValidationException ex) {
        log.error("Validación de antifraude fallida para solicitud {}: nivel={}, factores={}", 
            ex.getRequestId(), ex.getRiskLevel(), ex.getRiskFactors().size());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("requestId", ex.getRequestId().toString());
        metadata.put("riskLevel", ex.getRiskLevel().name());
        metadata.put("validationTime", ex.getValidationTime().toString());
        
        if (ex.hasRiskFactors()) {
            metadata.put("riskFactors", ex.getRiskFactors().stream()
                .map(factor -> Map.of(
                    "factor", factor.getFactor(),
                    "description", factor.getDescription(),
                    "score", factor.getScore()
                ))
                .collect(Collectors.toList()));
        }
        
        HttpStatus status = ex.isHighRisk() ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.BAD_REQUEST;
        String userMessage = ex.isHighRisk() 
            ? "La solicitud ha sido rechazada por políticas de seguridad" 
            : "La solicitud requiere revisión adicional";
        
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            ex.getErrorCode(),
            userMessage,
            Instant.now(),
            metadata
        );
        
        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        log.error("Error no manejado: ", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Ha ocurrido un error interno. Por favor, contacte al administrador.",
            Instant.now(),
            Map.of("errorId", java.util.UUID.randomUUID().toString())
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    private Map<String, Object> buildMetadata(CreditRequestNotFoundException ex) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("searchContext", ex.getSearchContext());
        metadata.put("timestamp", ex.getTimestamp().toString());
        
        if (ex.hasRequestId()) {
            metadata.put("requestId", ex.getRequestId().toString());
        }
        if (ex.hasIdempotencyKey()) {
            metadata.put("idempotencyKey", ex.getMessage().split("Clave de idempotencia: ")[1]);
        }
        
        return metadata;
    }

    public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        Map<String, Object> metadata
    ) {}
}