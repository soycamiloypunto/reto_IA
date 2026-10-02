package com.bank.credit.application.usecases;



import com.bank.credit.domain.ports.AntiFraudResult;
import com.bank.credit.domain.models.CreditRequestStatus;
import com.bank.credit.domain.models.CreditRequest;
import com.bank.credit.domain.ports.AntiFraudPort;
import com.bank.credit.domain.ports.CreditRequestPort;
import com.bank.credit.domain.ports.CoreBankingPort;
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
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessCreditRequestUseCase - Pruebas de integración del caso de uso principal")
class ProcessCreditRequestUseCaseTest {

    @Mock
    private CreditRequestPort creditRequestPort;

    @Mock
    private AntiFraudPort antiFraudPort;

    @Mock
    private CoreBankingPort coreBankingPort;

    @InjectMocks
    private ProcessCreditRequestUseCase processCreditRequestUseCase;

    private CreditRequest testCreditRequest;

    @BeforeEach
    void setUp() {
        testCreditRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-001",
            new BigDecimal("50000.00"),
            36,
            CreditRequest.CreditRequestStatus.PENDING,
            LocalDateTime.now(),
            null,
            "WEB",
            "OP-2024-001",
            null
        );
    }

    @Test
    @DisplayName("Debería procesar exitosamente una solicitud de crédito cuando pasa todas las validaciones")
    void shouldProcessCreditRequestSuccessfullyWhenAllValidationsPass() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));
        when(coreBankingPort.submitApplication(any(CreditRequest.class)))
            .thenReturn(Mono.just("CORE-12345"));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.APPROVED);
            })
            .verifyComplete();

        verify(creditRequestPort).save(any(CreditRequest.class));
        verify(antiFraudPort).validate(any(CreditRequest.class));
        verify(coreBankingPort).submitApplication(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería rechazar solicitud cuando el antifraude la marca como fraudulenta")
    void shouldRejectCreditRequestWhenAntiFraudDetectsFraud() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(false, "REJECTED", "Alto riesgo detectado")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.REJECTED);
                assertThat(result.rejectionReason()).contains("Alto riesgo");
            })
            .verifyComplete();

        verify(coreBankingPort, never()).submitApplication(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería devolver solicitud existente cuando la clave de idempotencia ya existe")
    void shouldReturnExistingRequestWhenIdempotencyKeyExists() {
        CreditRequest existingRequest = new CreditRequest(
            UUID.randomUUID(),
            "CLIENT-001",
            new BigDecimal("50000.00"),
            36,
            CreditRequest.CreditRequestStatus.APPROVED,
            LocalDateTime.now().minusHours(1),
            null,
            "WEB",
            "OP-2024-001",
            null
        );

        when(creditRequestPort.findByIdempotencyKey("OP-2024-001"))
            .thenReturn(Mono.just(existingRequest));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result.id()).isEqualTo(existingRequest.id());
                assertThat(result.status()).isEqualTo(CreditRequest.CreditRequestStatus.APPROVED);
            })
            .verifyComplete();

        verify(creditRequestPort, never()).save(any(CreditRequest.class));
        verify(antiFraudPort, never()).validate(any(CreditRequest.class));
    }

    @Test
    @DisplayName("Debería manejar correctamente cuando el core bancario no está disponible")
    void shouldHandleCoreBankingFailureGracefully() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "APPROVED", null)));
        when(coreBankingPort.submitApplication(any(CreditRequest.class)))
            .thenReturn(Mono.error(new RuntimeException("Core bancario no disponible")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .expectErrorMatches(throwable -> throwable.getMessage().contains("Core bancario"))
            .verify();
    }

    @Test
    @DisplayName("Debería continuar flujo cuando antifraude tiene timeout pero hay fallback configurado")
    void shouldContinueFlowWhenAntiFraudTimesOut() {
        when(creditRequestPort.findByIdempotencyKey(anyString())).thenReturn(Mono.empty());
        when(creditRequestPort.save(any(CreditRequest.class))).thenReturn(Mono.just(testCreditRequest));
        when(antiFraudPort.validate(any(CreditRequest.class)))
            .thenReturn(Mono.just(new AntiFraudPort.AntiFraudResult(true, "PENDING_REVIEW", "Timeout en verificación")));

        StepVerifier.create(processCreditRequestUseCase.execute(testCreditRequest))
            .assertNext(result -> {
                assertThat(result).isNotNull();
                assertThat(result.status()).isIn(
                    CreditRequest.CreditRequestStatus.PENDING,
                    CreditRequest.CreditRequestStatus.PENDING_REVIEW
                );
            })
            .verifyComplete();
    }
}