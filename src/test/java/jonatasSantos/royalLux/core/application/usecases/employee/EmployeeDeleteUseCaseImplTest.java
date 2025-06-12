package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeDeleteUseCaseImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeDeleteUseCaseImpl employeeDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir funcionário a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Funcionário inexistente'")
    void deveLancarExcecaoQuandoNaoExistirFuncionarioASerDeletado() {
        // Arrange
        when(employeeRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeDeleteUseCase.execute(3);
        });

        assertEquals("Funcionário inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar funcionário com sucesso e retornar true em propriedade success do output")
    void deveDeletarFuncionarioComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        Employee employee = new Employee(user, "Barbeiro", BigDecimal.valueOf(2000));

        when(employeeRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(employee));

        // Act
        EmployeeDeleteUseCaseOutputDto output = employeeDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(employeeRepository).delete(any(Employee.class));
        verify(employeeRepository, times(1)).delete(employee);
    }
}