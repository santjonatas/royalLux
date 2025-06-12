package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClientGetUseCaseImplTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Client> criteriaQuery;

    @Mock
    private Root<Client> root;

    @Mock
    private TypedQuery<Client> typedQuery;

    @InjectMocks
    private ClientGetUseCaseImpl clientGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = ClientGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(clientGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        ClientGetUseCaseInputDto input = new ClientGetUseCaseInputDto(null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientGetUseCase.execute(
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
    @DisplayName("Quando usuário for CLIENT, deve retornar apenas ele mesmo")
    void naoDeveRetornarOutrosClientesQuandoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);
        Client client1 = new Client(userLogged);
        client1.setId(1);

        ClientGetUseCaseInputDto input = new ClientGetUseCaseInputDto(null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user2 = new User("maria_client", UserRole.CLIENT, true);
        user2.setId(2);
        Client client2 = new Client(user2);
        client2.setId(2);

        var clientsFromDb = List.of(client1, client2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Client.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Client.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(clientsFromDb);

        // Act
        var result = clientGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(client -> client.id().equals(1))); // O próprio Client

        assertFalse(result.stream().anyMatch(client -> client.id().equals(2))); // Outro Client não deve aparecer
    }

    @Test
    @DisplayName("Quando usuário for ADMIN, deve retornar todos os clientes com sucesso")
    void deveRetornarTodosClientesQuandoForAdmin() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        ClientGetUseCaseInputDto input = new ClientGetUseCaseInputDto(null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user2 = new User("maria_client", UserRole.CLIENT, true);
        user2.setId(2);
        Client client1 = new Client(user2);
        client1.setId(2);

        var clientsFromDb = List.of(client1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Client.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Client.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(clientsFromDb);

        // Act
        var result = clientGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(client -> client.id().equals(2))); // Client
    }
}