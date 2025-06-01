package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
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

class UserGetUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<User> criteriaQuery;

    @Mock
    private Root<User> root;

    @Mock
    private TypedQuery<User> typedQuery;

    @InjectMocks
    private UserGetUseCaseImpl userGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = UserGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(userGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        UserGetUseCaseInputDto input = new UserGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userGetUseCase.execute(
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
    @DisplayName("Quando usuário for CLIENT, deve retornar apenas ele mesmo e excluir outros CLIENTS")
    void naoDeveRetornarOutrosUsuariosClientQuandoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);

        UserGetUseCaseInputDto input = new UserGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User client2 = new User("maria_client", UserRole.CLIENT, true);
        client2.setId(2);

        User employee = new User("carlos_employee", UserRole.EMPLOYEE, true);
        employee.setId(3);

        User admin = new User("ana_admin", UserRole.ADMIN, true);
        admin.setId(4);

        var usersFromDb = List.of(userLogged, client2, employee, admin);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(usersFromDb);

        // Act
        var result = userGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(user -> user.id().equals(1))); // O próprio Client
        assertTrue(result.stream().anyMatch(user -> user.id().equals(3))); // Employee
        assertTrue(result.stream().anyMatch(user -> user.id().equals(4))); // Admin

        assertFalse(result.stream().anyMatch(user -> user.id().equals(2))); // Outro Client não deve aparecer
    }

    @Test
    @DisplayName("Quando usuário for ADMIN, deve retornar todos os usuários com sucesso")
    void deveRetornarTodosUsuariosQuandoForAdmin() {
        // Arrange
        User userLogged = new User("ana_admin", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserGetUseCaseInputDto input = new UserGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User client = new User("joao_client", UserRole.CLIENT, true);
        client.setId(2);

        User employee = new User("carlos_employee", UserRole.EMPLOYEE, true);
        employee.setId(3);

        var usersFromDb = List.of(userLogged, client, employee);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(usersFromDb);

        // Act
        var result = userGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(user -> user.id().equals(1))); // Admin logado
        assertTrue(result.stream().anyMatch(user -> user.id().equals(2))); // Client
        assertTrue(result.stream().anyMatch(user -> user.id().equals(3))); // Employee
    }
}