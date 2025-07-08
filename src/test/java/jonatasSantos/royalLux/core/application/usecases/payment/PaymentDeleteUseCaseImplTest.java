package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
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

class PaymentDeleteUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentDeleteUseCaseImpl paymentDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando pagamento não existir com o ID informado, deve lançar EntityNotFoundException com mensagem 'Pagamento inexistente'")
    void deveLancarExcecaoQuandoPagamentoNaoExistir() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        when(paymentRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            paymentDeleteUseCase.execute(userLogged, 1);
        });

        assertEquals("Pagamento inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando pagamento existir, deve excluir com sucesso e retornar true no output")
    void deveDeletarPagamentoComSucesso() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        Payment pagamento = new Payment();
        pagamento.setId(1);

        when(paymentRepository.findById("1")).thenReturn(Optional.of(pagamento));

        // Act
        PaymentDeleteUseCaseOutputDto output = paymentDeleteUseCase.execute(userLogged, 1);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(paymentRepository, times(1)).delete(pagamento);
    }
}