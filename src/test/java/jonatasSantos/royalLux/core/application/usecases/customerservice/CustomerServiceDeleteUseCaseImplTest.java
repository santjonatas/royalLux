package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
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

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    @Mock
    PaymentRepository paymentRepository;

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
    @DisplayName("Quando atendimento não tiver vínculos, deve excluir atendimento com sucesso e retornar sucesso no output")
    void deveDeletarAtendimentoSemVinculosComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User funcionario = new User("marcos_2", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                funcionario,
                client,
                CustomerServiceStatus.AGENDADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService.setId(1);

        when(customerServiceRepository.findById("1")).thenReturn(Optional.of(customerService));
        when(salonServiceCustomerServiceRepository.existsByCustomerServiceId(1)).thenReturn(false);
        when(paymentRepository.existsByCustomerServiceId(1)).thenReturn(false);

        // Act
        CustomerServiceDeleteUseCaseOutputDto output = customerServiceDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(customerServiceRepository, times(1)).delete(customerService);
    }

    @Test
    @DisplayName("Quando atendimento possuir vínculos e pagamentos, deve excluí-los antes de excluir o atendimento")
    void deveDeletarVinculosEPagamentosAntesDeletarAtendimento() {
        // Arrange
        User user = new User("ana_maria", UserRole.CLIENT, true);
        Client client = new Client(user);

        User funcionario = new User("carlos_joao", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                funcionario,
                client,
                CustomerServiceStatus.AGENDADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(60),
                "obs"
        );
        customerService.setId(9);

        when(customerServiceRepository.findById("9")).thenReturn(Optional.of(customerService));
        when(salonServiceCustomerServiceRepository.existsByCustomerServiceId(9)).thenReturn(true);
        when(paymentRepository.existsByCustomerServiceId(9)).thenReturn(true);

        // Act
        CustomerServiceDeleteUseCaseOutputDto output = customerServiceDeleteUseCase.execute(9);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());

        verify(salonServiceCustomerServiceRepository, times(1)).deleteByCustomerServiceId(9);
        verify(paymentRepository, times(1)).deleteByCustomerServiceId(9);
        verify(customerServiceRepository, times(1)).delete(customerService);
    }

    @Test
    @DisplayName("Quando atendimento existir e não houver vínculos, deve excluir com sucesso e retornar true no output")
    void deveExecutarComSucessoQuandoNaoHouverVinculos() {
        // Arrange
        User user = new User("felipe_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User funcionario = new User("mari_silva", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                funcionario,
                client,
                CustomerServiceStatus.AGENDADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(80),
                ""
        );
        customerService.setId(8);

        when(customerServiceRepository.findById("8")).thenReturn(Optional.of(customerService));
        when(salonServiceCustomerServiceRepository.existsByCustomerServiceId(8)).thenReturn(false);
        when(paymentRepository.existsByCustomerServiceId(8)).thenReturn(false);

        // Act
        CustomerServiceDeleteUseCaseOutputDto output = customerServiceDeleteUseCase.execute(8);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());

        verify(customerServiceRepository, times(1)).findById("8");
        verify(salonServiceCustomerServiceRepository, times(1)).existsByCustomerServiceId(8);
        verify(paymentRepository, times(1)).existsByCustomerServiceId(8);
        verify(customerServiceRepository, times(1)).delete(customerService);
    }
}