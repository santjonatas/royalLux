package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalonServiceCustomerServiceDeleteUseCaseImplTest {

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

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
    @DisplayName("Deve deletar vínculo entre atendimento e serviço com sucesso e retornar true em propriedade success do output")
    void deveDeletarVinculoEntreAtendimentoEServicoComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        User user3 = new User("mateus_2", UserRole.EMPLOYEE, true);
        user3.setId(3);
        Employee employee = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee.setId(3);

        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService(customerService, salonService, employee, LocalDateTime.now(), LocalDateTime.now(), false);

        when(salonServiceCustomerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(salonServiceCustomerService));

        // Act
        SalonServiceCustomerServiceDeleteUseCaseOutputDto output = salonServiceCustomerServiceDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(salonServiceCustomerServiceRepository).delete(any(SalonServiceCustomerService.class));
        verify(salonServiceCustomerServiceRepository, times(1)).delete(salonServiceCustomerService);
    }
}