package com.bank.credit.infrastructure.adapters;



import com.bank.credit.domain.ports.AntiFraudResult;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AntiFraudAdapter - Pruebas del adaptador de servicio antifraude")
class AntiFraudAdapterTest {

    @Mock
    private AntiFraudPort antiFraudPort;

    @InjectMocks
    private AntiFraudAdapter antiFraudAdapter;

    private CreditRequest testCreditRequest;

    @BeforeEach
    void setUp() {
        testCreditRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-002",
            new BigDecimal("100000.00"),
            48,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "MOBILE",
            "OP-2024-002",
            null
        );
    }

    @Test
    @DisplayName("Debería aprobar solicitud cuando el análisis de riesgo es bajo")
    void shouldApproveRequestWhenRiskAnalysisIsLow() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("APPROVED");
                assertThat(result.reason()).isNull();
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería rechazar solicitud cuando el score de riesgo supera el umbral")
    void shouldRejectRequestWhenRiskScoreExceedsThreshold() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(false, "REJECTED", "Score de riesgo: 85/100")));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isFalse();
                assertThat(result.riskScore()).isEqualTo("REJECTED");
                assertThat(result.reason()).contains("85");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar timeout del servicio antifraude según configuración de Resilience4j")
    void shouldHandleAntiFraudServiceTimeout() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.delay(Duration.ofSeconds(3))
                .flatMap(l -> Mono.just(new AntiFraudPort.AntiFraudResult(true, "TIMEOUT_FALLBACK", null))));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest).timeout(Duration.ofSeconds(2)))
            .expectErrorMatches(throwable -> throwable.getMessage().contains("Timeout"))
            .verify();
    }

    @Test
    @DisplayName("Debería continuar con revisión manual cuando el servicio retorna error desconocido")
    void shouldContinueToManualReviewOnUnknownError() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeError("Servicio de riesgo no disponible")));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest)
                .onErrorResume(e -> Mono.just(new AntiFraudPort.AntiFraudResult(true, "MANUAL_REVIEW", "Error en servicio: " + e.getMessage()))))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("MANUAL_REVIEW");
                assertThat(result.reason()).contains("no disponible");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería marcar como revisión manual cuando el monto supera el límite automático")
    void shouldMarkForManualReviewWhenAmountExceedsAutomaticLimit() {
        CreditRequest highValueRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-003",
            new BigDecimal("500000.00"),
            60,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "BRANCH",
            "OP-2024-003",
            null
        );

        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "MANUAL_REVIEW", "Monto mayor a $200,000 requiere revisión manual")));

        StepVerifier.create(antiFraudAdapter.validate(highValueRequest))
            .assertNext(result -> {
                assertThat(result.approved()).isTrue();
                assertThat(result.riskScore()).isEqualTo("MANUAL_REVIEW");
                assertThat(result.reason()).contains("revisión manual");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería registrar métricas de latencia del servicio antifraude")
    void shouldRecordAntiFraudServiceLatencyMetrics() {
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> assertThat(result.approved()).isTrue())
            .verifyComplete();

        StepVerifier.create(antiFraudAdapter.validate(testCreditRequest))
            .assertNext(result -> assertThat(result.approved()).isTrue())
            .verifyComplete();
    }
}