package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentUpdateUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentUpdateUseCaseImpl paymentUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando usuário logado não existir, deve lançar EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User user = new User("joao_1", UserRole.EMPLOYEE, true); // nome corrigido com 6 caracteres
        user.setId(1);
        PaymentUpdateUseCaseInputDto input = new PaymentUpdateUseCaseInputDto(
                PaymentStatus.PAGO, "Desc", "Pagador");

        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            paymentUpdateUseCase.execute(user, 1, input);
        });

        assertEquals("Seu usuário é inexistente", ex.getMessage());
    }

    @Test
    @DisplayName("Quando pagamento não existir, deve lançar EntityNotFoundException com mensagem 'Pagamento é inexistente'")
    void deveLancarExcecaoQuandoPagamentoNaoExistir() {
        // Arrange
        User user = new User("joao_2", UserRole.EMPLOYEE, true); // nome corrigido com 6 caracteres
        user.setId(2);
        PaymentUpdateUseCaseInputDto input = new PaymentUpdateUseCaseInputDto(PaymentStatus.PAGO, "Desc", "Pagador");

        when(userRepository.findById("2")).thenReturn(Optional.of(user));
        when(paymentRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            paymentUpdateUseCase.execute(user, 1, input);
        });

        assertEquals("Pagamento é inexistente", ex.getMessage());
    }

    @Test
    @DisplayName("Quando funcionário tentar atualizar pagamento já finalizado, deve lançar IllegalArgumentException com mensagem 'Pagamento já foi finalizado'")
    void deveLancarExcecaoQuandoFuncionarioAtualizaPagamentoFinalizado() {
        // Arrange
        User funcionario = new User("lucas", UserRole.EMPLOYEE, true);
        funcionario.setId(3);

        Payment pagamento = new Payment();
        pagamento.setStatus(PaymentStatus.PAGO); // finalizado

        PaymentUpdateUseCaseInputDto input = new PaymentUpdateUseCaseInputDto(PaymentStatus.ESTORNADO, "Atualização", "Pagador");

        when(userRepository.findById("3")).thenReturn(Optional.of(funcionario));
        when(paymentRepository.findById("1")).thenReturn(Optional.of(pagamento));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            paymentUpdateUseCase.execute(funcionario, 1, input);
        });

        assertEquals("Pagamento já foi finalizado", ex.getMessage());
    }

    @Test
    @DisplayName("Quando atualização for válida, deve atualizar pagamento e retornar true no output")
    void deveAtualizarPagamentoComSucesso() {
        // Arrange
        User user = new User("admin", UserRole.ADMIN, true);
        user.setId(4);

        Payment pagamento = new Payment();
        pagamento.setStatus(PaymentStatus.PENDENTE);
        pagamento.setId(1);

        PaymentUpdateUseCaseInputDto input = new PaymentUpdateUseCaseInputDto(PaymentStatus.ESTORNADO, "Reembolso solicitado", "Eduarda");

        when(userRepository.findById("4")).thenReturn(Optional.of(user));
        when(paymentRepository.findById("1")).thenReturn(Optional.of(pagamento));
        when(paymentRepository.save(any(Payment.class))).thenReturn(pagamento);

        // Act
        PaymentUpdateUseCaseOutputDto output = paymentUpdateUseCase.execute(user, 1, input);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        assertEquals(0, output.warningList().size());
        verify(paymentRepository, times(1)).save(pagamento);
    }
}