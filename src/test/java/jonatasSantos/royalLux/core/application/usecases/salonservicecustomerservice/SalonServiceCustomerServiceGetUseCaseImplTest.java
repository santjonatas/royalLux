package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseInputDto;
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
import static org.mockito.Mockito.when;

class SalonServiceCustomerServiceGetUseCaseImplTest {

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
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<SalonServiceCustomerService> criteriaQuery;

    @Mock
    private Root<SalonServiceCustomerService> root;

    @Mock
    private TypedQuery<SalonServiceCustomerService> typedQuery;

    @InjectMocks
    private SalonServiceCustomerServiceGetUseCaseImpl salonServiceCustomerServiceGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = SalonServiceCustomerServiceGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(salonServiceCustomerServiceGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        SalonServiceCustomerServiceGetUseCaseInputDto input = new SalonServiceCustomerServiceGetUseCaseInputDto(null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário for cliente, deve retornar apenas os vínculos de serviços do próprio atendimento")
    void naoDeveRetornarVinculoEntreServicosEAtendimentoDeOutrosUsuariosQuandoForCliente() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);

        Client client2 = new Client(userLogged);
        client2.setId(2);

        SalonServiceCustomerServiceGetUseCaseInputDto input = new SalonServiceCustomerServiceGetUseCaseInputDto(null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        Client client = new Client(user);
        client.setId(1);

        User user3 = new User("marcos_2", UserRole.EMPLOYEE, true);
        user3.setId(3);

        CustomerService customerService1 = new CustomerService(
                user3,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService1.setId(1);

        CustomerService customerService2 = new CustomerService(
                user3,
                client2,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService2.setId(2);

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        Employee employee = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee.setId(1);

        SalonServiceCustomerService salonServiceCustomerService1 = new SalonServiceCustomerService(
                customerService1, salonService, employee,
                LocalDate.now(), LocalTime.now(), false
        );
        salonServiceCustomerService1.setId(1);

        SalonServiceCustomerService salonServiceCustomerService2 = new SalonServiceCustomerService(
                customerService2, salonService, employee,
                LocalDate.now(), LocalTime.now(), false
        );
        salonServiceCustomerService2.setId(2);

        var salonServicesCustomerServicesFromDb = List.of(
                salonServiceCustomerService1,
                salonServiceCustomerService2
        );

        // Mocks do EntityManager e Query
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(SalonServiceCustomerService.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(SalonServiceCustomerService.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(salonServicesCustomerServicesFromDb);

        // Mocks dos customerServices usados no filtro do usecase
        when(customerServiceRepository.findById("1")).thenReturn(Optional.of(customerService1));
        when(customerServiceRepository.findById("2")).thenReturn(Optional.of(customerService2));

        // Act
        var result = salonServiceCustomerServiceGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(salonServiceCustomerService -> salonServiceCustomerService.id().equals(2)));
    }

    @Test
    @DisplayName("Quando usuário for admin, deve retornar todos os vínculos entre serviços e atendimento")
    void deveRetornarTodosVinculosEntreAtendimentoEServico() {
        // Arrange
        User userLogged = new User("joao_0", UserRole.ADMIN, true);
        userLogged.setId(1);

        User user1 = new User("vitor_1", UserRole.CLIENT, true);
        Client client2 = new Client(user1);
        client2.setId(2);

        SalonServiceCustomerServiceGetUseCaseInputDto input = new SalonServiceCustomerServiceGetUseCaseInputDto(null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Client client = new Client(user);
        client.setId(1);

        User user3 = new User("marcos_2", UserRole.EMPLOYEE, true);
        user3.setId(3);

        CustomerService customerService1 = new CustomerService(
                user3,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService1.setId(1);

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        Employee employee = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee.setId(1);

        SalonServiceCustomerService salonServiceCustomerService1 = new SalonServiceCustomerService(customerService1, salonService, employee, LocalDate.now(), LocalTime.now(), false);
        salonServiceCustomerService1.setId(1);

        CustomerService customerService2 = new CustomerService(
                user3,
                client2,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService2.setId(2);

        SalonServiceCustomerService salonServiceCustomerService2 = new SalonServiceCustomerService(customerService2, salonService, employee, LocalDate.now(), LocalTime.now(), false);
        salonServiceCustomerService2.setId(2);

        var salonServicesCustomerServicesFromDb = List.of(salonServiceCustomerService1, salonServiceCustomerService2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(SalonServiceCustomerService.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(SalonServiceCustomerService.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(salonServicesCustomerServicesFromDb);

        // Act
        var result = salonServiceCustomerServiceGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(salonServiceCustomerService -> salonServiceCustomerService.id().equals(1))); // SalonServiceCustomerService 1
        assertTrue(result.stream().anyMatch(salonServiceCustomerService -> salonServiceCustomerService.id().equals(2))); // SalonServiceCustomerService 2
    }
}