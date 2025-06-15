package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
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

class CustomerServiceDeleteUseCaseImplTest {

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @InjectMocks
    private CustomerServiceDeleteUseCaseImpl customerServiceDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir atendimento a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Atendimento inexistente'")
    void deveLancarExcecaoQuandoNaoExistirAtendimentoASerDeletado() {
        // Arrange
        when(customerServiceRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceDeleteUseCase.execute(3);
        });

        assertEquals("Atendimento inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar atendimento com sucesso e retornar true em propriedade success do output")
    void deveDeletarAtendimentoComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.AGENDADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
                );

        when(customerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(customerService));

        // Act
        CustomerServiceDeleteUseCaseOutputDto output = customerServiceDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(customerServiceRepository).delete(any(CustomerService.class));
        verify(customerServiceRepository, times(1)).delete(customerService);
    }
}