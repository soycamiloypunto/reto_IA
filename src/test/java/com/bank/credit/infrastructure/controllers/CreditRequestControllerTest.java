package com.bank.credit.infrastructure.controllers;


import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.application.usecases.ProcessCreditRequestUseCase;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.infrastructure.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditRequestController - Pruebas del controlador REST con WebTestClient")
class CreditRequestControllerTest {

    @Mock
    private ProcessCreditRequestUseCase processCreditRequestUseCase;

    @InjectMocks
    private CreditRequestController creditRequestController;

    private WebTestClient webTestClient;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new GlobalExceptionHandler(), creditRequestController)
            .configureClient()
            .baseUrl("http://localhost:8080")
            .build();
    }

    @Test
    @DisplayName("Debería crear solicitud de crédito exitosamente con código 201 Created")
    void shouldCreateCreditRequestSuccessfullyWith201Created() {
        CreditRequest request = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-100",
            new BigDecimal("75000.00"),
            36,
            CreditRequest.CreditRequestStatus.APPROVED,
            LocalDateTime.now(),
            null,
            "WEB",
            "OP-2024-100",
            null
        );

        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.just(request));

        String requestBody = """
            {
                "clientId": "CLIENT-100",
                "amount": 75000.00,
                "termMonths": 36,
                "channel": "WEB",
                "idempotencyKey": "OP-2024-100"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.clientId").isEqualTo("CLIENT-100")
            .jsonPath("$.amount").isEqualTo(75000.00)
            .jsonPath("$.status").isEqualTo("APPROVED");
    }

    @Test
    @DisplayName("Debería devolver 400 Bad Request cuando los datos de entrada son inválidos")
    void shouldReturn400BadRequestWhenInputDataIsInvalid() {
        String invalidRequest = """
            {
                "clientId": "",
                "amount": -1000.00,
                "termMonths": 0,
                "channel": "UNKNOWN_CHANNEL",
                "idempotencyKey": ""
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(invalidRequest))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.message").exists();
    }

    @Test
    @DisplayName("Debería devolver 409 Conflict cuando la clave de idempotencia ya existe")
    void shouldReturn409ConflictWhenIdempotencyKeyAlreadyExists() {
        CreditRequest existingRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-100",
            new BigDecimal("75000.00"),
            36,
            CreditRequest.CreditRequestStatus.REJECTED,
            LocalDateTime.now().minusHours(2),
            "Riesgo detectado",
            "WEB",
            "OP-2024-DUPLICATE",
            null
        );

        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.just(existingRequest));

        String requestBody = """
            {
                "clientId": "CLIENT-100",
                "amount": 75000.00,
                "termMonths": 36,
                "channel": "WEB",
                "idempotencyKey": "OP-2024-DUPLICATE"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody()
            .jsonPath("$.error").isEqualTo("CONFLICT")
            .jsonPath("$.message").exists();
    }

    @Test
    @DisplayName("Debería devolver 503 Service Unavailable cuando el servicio antifraude falla")
    void shouldReturn503WhenAntiFraudServiceFails() {
        when(processCreditRequestUseCase.execute(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeException("Servicio antifraude no disponible")));

        String requestBody = """
            {
                "clientId": "CLIENT-200",
                "amount": 50000.00,
                "termMonths": 24,
                "channel": "MOBILE",
                "idempotencyKey": "OP-2024-200"
            }
            """;

        webTestClient.post()
            .uri("/api/v1/credit-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(requestBody))
            .exchange()
            .expectStatus().isServiceUnavailable()
            .expectBody()
            .jsonPath("$.error").isEqualTo("SERVICE_UNAVAILABLE")
            .jsonPath("$.message").contains("antifraude");
    }

    @Test
n    @DisplayName("Debería obtener solicitud de crédito por ID exitosamente")
    void shouldGetCreditRequestByIdSuccessfully() {
        UUID requestId = UUID.randomUUID();
        CreditRequest foundRequest = new CreditRequest(
            requestId,
            "CLIENT-300",
            new BigDecimal("120000.00"),
            48,
            CreditRequest.CreditRequestStatus.PENDING_REVIEW,
            LocalDateTime.now(),
            null,
            "BRANCH",
            "OP-2024-300",
            null
        );

        when(processCreditRequestUseCase.getById(requestId))
            .thenReturn(Mono.just(foundRequest));

        webTestClient.get()
            .uri("/api/v1/credit-requests/{id}", requestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(requestId.toString())
            .jsonPath("$.clientId").isEqualTo("CLIENT-300")
            .jsonPath("$.status").isEqualTo("PENDING_REVIEW");
    }

    @Test
    @DisplayName("Debería devolver 404 Not Found cuando la solicitud no existe")
    void shouldReturn404WhenCreditRequestNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(processCreditRequestUseCase.getById(nonExistentId))
            .thenReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/v1/credit-requests/{id}", nonExistentId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("NOT_FOUND");
    }
}