package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerServiceGetUseCaseImplTest {

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<CustomerService> criteriaQuery;

    @Mock
    private Root<CustomerService> root;

    @Mock
    private TypedQuery<CustomerService> typedQuery;

    @InjectMocks
    private CustomerServiceGetUseCaseImpl customerServiceGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = CustomerServiceGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(customerServiceGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        CustomerServiceGetUseCaseInputDto input = new CustomerServiceGetUseCaseInputDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceGetUseCase.execute(
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
    @DisplayName("Quando usuário logado for cliente, deve retornar apenas os prórios atendimentos")
    void deveRetornarApenasOsPropriosAtendimentosQuandoUsuarioForCliente() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);
        Client client = new Client(userLogged);
        client.setId(1);

        CustomerServiceGetUseCaseInputDto input = new CustomerServiceGetUseCaseInputDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);
        user2.setId(2);

        CustomerService customerService1 = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService1.setId(1);

        User user3 = new User("ana_3", UserRole.CLIENT, true);
        user3.setId(3);
        Client client2 = new Client(user3);
        client2.setId(2);

        CustomerService customerService2 = new CustomerService(
                user2,
                client2,
                CustomerServiceStatus.EM_ANDAMENTO,
                LocalDateTime.of(2025, 6, 10, 14, 30),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                LocalDateTime.of(2025, 6, 10, 16, 0),
                BigDecimal.valueOf(75.50),
                "Aplicação de hidratação capilar"
        );
        customerService2.setId(2);

        var rolesFromDb = List.of(customerService1, customerService2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(CustomerService.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(CustomerService.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(rolesFromDb);

        // Act
        var result = customerServiceGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(customerService -> customerService.id().equals(1))); // Próprio Atendimento

        assertFalse(result.stream().anyMatch(customerService -> customerService.id().equals(2))); // Atendimento de outro cliente
    }

    @Test
    @DisplayName("Quando usuário logado for admin, deve retornar todos os atendimentos")
    void deveRetornarTodosOsAtendimentosQuandoUsuarioForAdmin() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        CustomerServiceGetUseCaseInputDto input = new CustomerServiceGetUseCaseInputDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("pedroo_1", UserRole.CLIENT, true);
        Client client = new Client(user);
        client.setId(1);

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);
        user2.setId(2);

        CustomerService customerService1 = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );
        customerService1.setId(1);

        User user3 = new User("ana_3", UserRole.CLIENT, true);
        user3.setId(3);
        Client client2 = new Client(user3);
        client2.setId(2);

        CustomerService customerService2 = new CustomerService(
                user2,
                client2,
                CustomerServiceStatus.EM_ANDAMENTO,
                LocalDateTime.of(2025, 6, 10, 14, 30),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                LocalDateTime.of(2025, 6, 10, 16, 0),
                BigDecimal.valueOf(75.50),
                "Aplicação de hidratação capilar"
        );
        customerService2.setId(2);

        var rolesFromDb = List.of(customerService1, customerService2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(CustomerService.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(CustomerService.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(rolesFromDb);

        // Act
        var result = customerServiceGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(customerService -> customerService.id().equals(1))); // Atendimento
        assertTrue(result.stream().anyMatch(customerService -> customerService.id().equals(2))); // Atendimento
    }
}