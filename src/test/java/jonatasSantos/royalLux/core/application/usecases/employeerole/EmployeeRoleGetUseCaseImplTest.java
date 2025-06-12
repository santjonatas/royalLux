package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmployeeRoleGetUseCaseImplTest {

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<EmployeeRole> criteriaQuery;

    @Mock
    private Root<EmployeeRole> root;

    @Mock
    private TypedQuery<EmployeeRole> typedQuery;

    @InjectMocks
    private EmployeeRoleGetUseCaseImpl employeeRoleGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = EmployeeRoleGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(employeeRoleGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        EmployeeRoleGetUseCaseInputDto input = new EmployeeRoleGetUseCaseInputDto(null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeRoleGetUseCase.execute(
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
    @DisplayName("Deve retornar todos os vínculos entre funcionário e função com sucesso")
    void deveRetornarTodosVinculosEntreFuncaoEFuncionario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);

        EmployeeRoleGetUseCaseInputDto input = new EmployeeRoleGetUseCaseInputDto(null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");
        EmployeeRole employeeRole1 = new EmployeeRole(employee, role);
        employeeRole1.setId(1);

        var employeesRolesFromDb = List.of(employeeRole1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeRole.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeRole.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(employeesRolesFromDb);

        // Act
        var result = employeeRoleGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(employeeRole -> employeeRole.id().equals(1))); // EmployeeRole
    }
}