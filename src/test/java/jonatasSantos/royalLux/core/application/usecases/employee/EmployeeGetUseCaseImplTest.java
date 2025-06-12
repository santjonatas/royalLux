package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.User;
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

class EmployeeGetUseCaseImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Employee> criteriaQuery;

    @Mock
    private Root<Employee> root;

    @Mock
    private TypedQuery<Employee> typedQuery;

    @InjectMocks
    private EmployeeGetUseCaseImpl employeeGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = EmployeeGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(employeeGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        EmployeeGetUseCaseInputDto input = new EmployeeGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeGetUseCase.execute(
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
    @DisplayName("Quando usuário for EMPLOYEE, salários de outros funcionários devem ser nulos")
    void naoDeveRetornarSalariosDeOutrosFuncionariosQuandoForEmployee() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);
        userLogged.setId(2);
        EmployeeGetUseCaseInputDto input = new EmployeeGetUseCaseInputDto(null, null, null, null);
        Employee employee1 = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        employee1.setId(2);

        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        user.setId(3);
        Employee employee2 = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee2.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        var employeesFromDb = List.of(employee1, employee2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Employee.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Employee.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(employeesFromDb);

        // Act
        var result = employeeGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(employee -> employee.id().equals(2))); // O próprio Employee
        assertTrue(result.stream().anyMatch(employee -> employee.salary().equals(BigDecimal.valueOf(2000))));

        assertTrue(result.stream().anyMatch(employee -> employee.id().equals(3))); // Outro Employee
        assertTrue(result.stream().anyMatch(employee -> employee.salary() == null));
    }

    @Test
    @DisplayName("Quando usuário for CLIENT, salários de funcionários devem ser nulos")
    void naoDeveRetornarSalariosDeFuncionariosQuandoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(2);
        EmployeeGetUseCaseInputDto input = new EmployeeGetUseCaseInputDto(null, null, null, null);

        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        user.setId(3);
        Employee employee2 = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee2.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        var employeesFromDb = List.of(employee2);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Employee.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Employee.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(employeesFromDb);

        // Act
        var result = employeeGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(employee -> employee.id().equals(3))); // Employee
        assertTrue(result.stream().anyMatch(employee -> employee.salary() == null));
    }
}