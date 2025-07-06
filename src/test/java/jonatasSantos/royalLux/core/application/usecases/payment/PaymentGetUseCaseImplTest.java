package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentGetUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Payment> criteriaQuery;

    @Mock
    private Root<Payment> root;

    @Mock
    private TypedQuery<Payment> typedQuery;

    @InjectMocks
    private PaymentGetUseCaseImpl paymentGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = PaymentGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(paymentGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        PaymentGetUseCaseInputDto input = new PaymentGetUseCaseInputDto(null, null, null, null, null, null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId()))).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            paymentGetUseCase.execute(userLogged, input, 0, 10, true);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário for CLIENT, apenas seus próprios pagamentos devem ser retornados")
    void deveFiltrarPagamentosDoClienteQuandoUsuarioForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);

        PaymentGetUseCaseInputDto input = new PaymentGetUseCaseInputDto(
                null, null, null, null, null, null, null, null, null, null
        );

        // Mocks
        CustomerService customerService = mock(CustomerService.class);
        Client client = mock(Client.class);

        when(customerService.getId()).thenReturn(10);
        when(customerService.getClient()).thenReturn(client);
        when(client.getUser()).thenReturn(userLogged);

        Payment payment = new Payment();
        payment.setId(1);
        payment.setCustomerService(customerService);
        payment.setCreatedByUser(userLogged);
        payment.setPayerName("João");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById("1")).thenReturn(Optional.of(userLogged));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Payment.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Payment.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(payment));
        when(customerServiceRepository.findById("10")).thenReturn(Optional.of(customerService));

        // Act
        var result = paymentGetUseCase.execute(userLogged, input, 0, 10, false);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("João", result.get(0).payerName());
    }

    @Test
    @DisplayName("Quando usuário for ADMIN, deve retornar todos os pagamentos sem filtro por cliente")
    void deveRetornarTodosPagamentosQuandoUsuarioForAdmin() {
        // Arrange
        User userLogged = new User("admin_1", UserRole.ADMIN, true);
        userLogged.setId(99);

        PaymentGetUseCaseInputDto input = new PaymentGetUseCaseInputDto(null, null, null, null, null, null, null, null, null, null);

        Payment payment = new Payment();
        payment.setId(1);
        payment.setCreatedByUser(userLogged);
        payment.setPayerName("Carlos");

        CustomerService customerService = mock(CustomerService.class);
        customerService.setId(20);

        payment.setCustomerService(customerService);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById("99")).thenReturn(Optional.of(userLogged));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Payment.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Payment.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(payment));

        // Act
        var result = paymentGetUseCase.execute(userLogged, input, 0, 10, true);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("Carlos", result.get(0).payerName());
    }
}