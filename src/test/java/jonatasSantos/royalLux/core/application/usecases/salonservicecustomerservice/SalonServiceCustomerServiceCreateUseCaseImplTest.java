package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseOutputDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalonServiceCustomerServiceCreateUseCaseImplTest {
    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @InjectMocks
    private SalonServiceCustomerServiceCreateUseCaseImpl salonServiceCustomerServiceCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceCreateUseCaseInputDto input = new SalonServiceCustomerServiceCreateUseCaseInputDto(1, 1, 1, LocalTime.now(), false);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir atendimento com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Atendimento é inexistente'")
    void deveLancarExcecaoQuandoAtendimentoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceCreateUseCaseInputDto input = new SalonServiceCustomerServiceCreateUseCaseInputDto(1, 1, 1, LocalTime.now(), false);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(customerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Atendimento é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir serviço com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Serviço é inexistente'")
    void deveLancarExcecaoQuandoServicoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceCreateUseCaseInputDto input = new SalonServiceCustomerServiceCreateUseCaseInputDto(1, 1, 1, LocalTime.now(), false);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        CustomerService customerService = new CustomerService(
                userLogged,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );

        when(customerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(customerService));

        when(salonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Serviço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir funcionário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Funcionário é inexistente'")
    void deveLancarExcecaoQuandoFuncionarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceCreateUseCaseInputDto input = new SalonServiceCustomerServiceCreateUseCaseInputDto(1, 1, 1, LocalTime.now(), false);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        CustomerService customerService = new CustomerService(
                userLogged,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );

        when(customerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(customerService));

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        when(salonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(salonService));

        when(employeeRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Funcionário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando dados forem válidos, deve criar vínculo entre atendimento e serviço com sucesso e retornar ID do vínculo criado")
    void deveCriarVinculoEntreAtendimentoEServicoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(99);

        LocalDateTime atendimentoInicio = LocalDateTime.of(2025, 7, 5, 10, 0);
        LocalTime inicioServico = LocalTime.of(10, 15);

        SalonServiceCustomerServiceCreateUseCaseInputDto input =
                new SalonServiceCustomerServiceCreateUseCaseInputDto(1, 1, 1, inicioServico, false);

        when(userRepository.findById("99")).thenReturn(Optional.of(userLogged));

        User userClient = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(userClient);

        CustomerService customerService = new CustomerService(
                userLogged,
                client,
                CustomerServiceStatus.EM_ANDAMENTO,
                atendimentoInicio,
                atendimentoInicio,
                atendimentoInicio,
                BigDecimal.valueOf(40),
                ""
        );

        when(customerServiceRepository.findById("1")).thenReturn(Optional.of(customerService));

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.of(0, 45), BigDecimal.valueOf(40));
        salonService.setId(1);
        when(salonServiceRepository.findById("1")).thenReturn(Optional.of(salonService));
        when(materialSalonServiceRepository.findBySalonServiceId(1)).thenReturn(Collections.emptyList());

        User userEmployee = new User("vitor_2", UserRole.EMPLOYEE, true);
        userEmployee.setId(3);
        Employee employee = new Employee(userEmployee, "Cabelereiro", BigDecimal.valueOf(2000));
        employee.setId(1);
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
        when(salonServiceCustomerServiceRepository.findByEmployeeIdAndDate(1, atendimentoInicio.toLocalDate()))
                .thenReturn(Collections.emptyList());

        when(salonServiceCustomerServiceRepository.save(any(SalonServiceCustomerService.class)))
                .thenAnswer(invocation -> {
                    SalonServiceCustomerService service = invocation.getArgument(0);
                    service.setId(1);
                    return service;
                });

        SalonServiceCustomerService vinculoExistente = mock(SalonServiceCustomerService.class);
        when(vinculoExistente.getEstimatedFinishingTime()).thenReturn(LocalTime.of(11, 0));

        when(salonServiceCustomerServiceRepository.findByCustomerServiceId(customerService.getId()))
                .thenReturn(List.of(vinculoExistente));

        // Act
        SalonServiceCustomerServiceCreateUseCaseOutputDto output = salonServiceCustomerServiceCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.salonServiceCustomerServiceId());

        verify(userRepository, times(1)).findById("99");
        verify(customerServiceRepository, times(1)).findById("1");
        verify(salonServiceRepository, times(1)).findById("1");
        verify(employeeRepository, times(1)).findById("1");
        verify(salonServiceCustomerServiceRepository, times(1)).save(any(SalonServiceCustomerService.class));
        verify(customerServiceRepository, times(1)).save(any(CustomerService.class));
    }
}