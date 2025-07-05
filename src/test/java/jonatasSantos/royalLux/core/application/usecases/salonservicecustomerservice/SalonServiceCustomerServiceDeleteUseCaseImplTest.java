package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCaseOutputDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalonServiceCustomerServiceDeleteUseCaseImplTest {

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @InjectMocks
    private SalonServiceCustomerServiceDeleteUseCaseImpl salonServiceCustomerServiceDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir vínculo entre atendimento e serviço a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Vínculo entre serviço, atendimento e funcionário é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirVinculoEntreAtendimentoEServicoASerDeletado() {
        // Arrange
        when(salonServiceCustomerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceDeleteUseCase.execute(1);
        });

        assertEquals("Vínculo entre serviço, atendimento e funcionário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando vínculo for válido, deve deletar com sucesso e retornar true no output")
    void deveDeletarVinculoEntreAtendimentoEServicoComSucesso() {
        // Arrange
        User userCliente = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(userCliente);

        User userFuncionario = new User("marcos_2", UserRole.EMPLOYEE, true);
        CustomerService customerService = new CustomerService(
                userFuncionario,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.of(2025, 7, 5, 10, 0),
                LocalDateTime.of(2025, 7, 5, 10, 0),
                LocalDateTime.of(2025, 7, 5, 11, 0),
                BigDecimal.valueOf(100),
                ""
        );
        customerService.setId(10);

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.of(0, 45), BigDecimal.valueOf(40));
        salonService.setId(1);

        Employee employee = new Employee(userFuncionario, "Barbeiro", BigDecimal.valueOf(2000));
        employee.setId(5);

        SalonServiceCustomerService vinculo = new SalonServiceCustomerService(
                customerService,
                salonService,
                employee,
                customerService.getStartTime().toLocalDate(),
                LocalTime.of(10, 15),
                false
        );
        vinculo.setId(1);

        SalonServiceCustomerService outroServico = mock(SalonServiceCustomerService.class);
        when(outroServico.getEstimatedFinishingTime()).thenReturn(LocalTime.of(11, 0));

        when(salonServiceCustomerServiceRepository.findById("1")).thenReturn(Optional.of(vinculo));
        when(customerServiceRepository.findById("10")).thenReturn(Optional.of(customerService));
        when(salonServiceRepository.findById("1")).thenReturn(Optional.of(salonService));
        when(salonServiceCustomerServiceRepository.findByCustomerServiceId(10))
                .thenReturn(List.of(outroServico));

        // Act
        SalonServiceCustomerServiceDeleteUseCaseOutputDto output = salonServiceCustomerServiceDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(salonServiceCustomerServiceRepository, times(1)).delete(vinculo);
        verify(customerServiceRepository, times(1)).save(customerService);
    }
}