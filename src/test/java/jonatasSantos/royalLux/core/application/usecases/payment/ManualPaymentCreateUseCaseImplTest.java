package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManualPaymentCreateUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @InjectMocks
    private ManualPaymentCreateUseCaseImpl manualPaymentCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando usuário logado não existir, deve lançar EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true);
        ManualPaymentCreateUseCaseInputDto input = new ManualPaymentCreateUseCaseInputDto(1, PaymentStatus.PAGO, "Descrição", "Cliente A");

        when(userRepository.findById(String.valueOf(user.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            manualPaymentCreateUseCase.execute(user, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atendimento não existir, deve lançar EntityNotFoundException com mensagem 'Atendimento inexistente'")
    void deveLancarExcecaoQuandoAtendimentoNaoExistir() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true);
        user.setId(10);
        ManualPaymentCreateUseCaseInputDto input = new ManualPaymentCreateUseCaseInputDto(1, PaymentStatus.PAGO, "Descrição", "Cliente A");

        when(userRepository.findById("10")).thenReturn(Optional.of(user));
        when(customerServiceRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            manualPaymentCreateUseCase.execute(user, input);
        });

        assertEquals("Atendimento inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando pagamento já existir para o atendimento, deve lançar ConflictException com mensagem 'Pagamento já foi realizado'")
    void deveLancarExcecaoQuandoPagamentoJaExistir() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true);
        user.setId(10);

        User clientUser = new User("cliente_1", UserRole.CLIENT, true);
        Client client = new Client(clientUser);

        LocalDateTime now = LocalDateTime.of(2025, 7, 5, 10, 0);

        CustomerService atendimento = new CustomerService(
                user,
                client,
                CustomerServiceStatus.FINALIZADO,
                now,
                now,
                now,
                BigDecimal.valueOf(100),
                ""
        );
        atendimento.setId(1);

        ManualPaymentCreateUseCaseInputDto input = new ManualPaymentCreateUseCaseInputDto(1, PaymentStatus.PAGO, "Pagamento adiantado", "Cliente 1");

        when(userRepository.findById("10")).thenReturn(Optional.of(user));
        when(customerServiceRepository.findById("1")).thenReturn(Optional.of(atendimento));
        when(paymentRepository.existsByCustomerServiceId(1)).thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            manualPaymentCreateUseCase.execute(user, input);
        });

        assertEquals("Pagamento já foi realizado", exception.getMessage());
    }

    @Test
    @DisplayName("Quando status não for CASH_STATUSES, deve lançar IllegalArgumentException com mensagem 'Status inválido para este método de pagamento'")
    void deveLancarExcecaoQuandoStatusNaoForValidoParaPagamentoManual() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true);
        user.setId(10);

        LocalDateTime data = LocalDateTime.of(2025, 7, 5, 10, 0);

        CustomerService atendimento = new CustomerService(
                user,
                new Client(new User("cliente_2", UserRole.CLIENT, true)),
                CustomerServiceStatus.EM_ANDAMENTO,
                data,
                data,
                data,
                BigDecimal.valueOf(90),
                ""
        );
        atendimento.setId(2);

        ManualPaymentCreateUseCaseInputDto input = new ManualPaymentCreateUseCaseInputDto(2, PaymentStatus.ESTORNADO, "Tentativa inválida", "Cliente 2");

        when(userRepository.findById("10")).thenReturn(Optional.of(user));
        when(customerServiceRepository.findById("2")).thenReturn(Optional.of(atendimento));
        when(paymentRepository.existsByCustomerServiceId(2)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            manualPaymentCreateUseCase.execute(user, input);
        });

        assertEquals("Status inválido para este método de pagamento", exception.getMessage());
    }

    @Test
    @DisplayName("Quando dados forem válidos, deve criar pagamento manual com sucesso e retornar ID")
    void deveCriarPagamentoManualComSucesso() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true);
        user.setId(10);

        LocalDateTime data = LocalDateTime.of(2025, 7, 5, 10, 0);

        CustomerService atendimento = new CustomerService(
                user,
                new Client(new User("cliente_3", UserRole.CLIENT, true)),
                CustomerServiceStatus.FINALIZADO,
                data,
                data,
                data,
                BigDecimal.valueOf(120),
                ""
        );
        atendimento.setId(3);

        ManualPaymentCreateUseCaseInputDto input = new ManualPaymentCreateUseCaseInputDto(3, PaymentStatus.PAGO, "Pagamento concluído", "Cliente 3");

        when(userRepository.findById("10")).thenReturn(Optional.of(user));
        when(customerServiceRepository.findById("3")).thenReturn(Optional.of(atendimento));
        when(paymentRepository.existsByCustomerServiceId(3)).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(101);
            return p;
        });

        // Act
        ManualPaymentCreateUseCaseOutputDto output = manualPaymentCreateUseCase.execute(user, input);

        // Assert
        assertNotNull(output);
        assertEquals(101, output.paymentId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(userRepository, times(1)).findById("10");
        verify(customerServiceRepository, times(1)).findById("3");
    }
}