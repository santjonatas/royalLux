package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.EmployeeRole;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeDeleteUseCaseImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

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
    @DisplayName("Quando funcionário possuir funções vinculadas, estourar exceção IllegalStateException com mensagem específica")
    void deveLancarExcecaoQuandoFuncionarioPossuirFuncoesVinculadas() {
        // Arrange
        User user = new User("aninha", UserRole.EMPLOYEE, true);
        Employee employee = new Employee(user, "Recepcionista", BigDecimal.valueOf(1500));
        employee.setId(5);

        when(employeeRepository.findById("5")).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findByEmployeeId(5)).thenReturn(List.of(mock(EmployeeRole.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            employeeDeleteUseCase.execute(5);
        });

        assertEquals("Funcionário não pode ser deletado pois ainda possui funções vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando funcionário possuir serviços de atendimentos vinculados, estourar exceção IllegalStateException com mensagem específica")
    void deveLancarExcecaoQuandoFuncionarioPossuirServicosAtendimentosVinculados() {
        // Arrange
        User user = new User("carolina", UserRole.EMPLOYEE, true);
        Employee employee = new Employee(user, "Manicure", BigDecimal.valueOf(1800));
        employee.setId(7);

        when(employeeRepository.findById("7")).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findByEmployeeId(7)).thenReturn(Collections.emptyList());
        when(salonServiceCustomerServiceRepository.findByEmployeeId(7))
                .thenReturn(List.of(mock(SalonServiceCustomerService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            employeeDeleteUseCase.execute(7);
        });

        assertEquals("Funcionário não pode ser deletado pois ainda possui serviços de atendimentos vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar funcionário com sucesso e retornar true em propriedade success do output")
    void deveDeletarFuncionarioComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        Employee employee = new Employee(user, "Barbeiro", BigDecimal.valueOf(2000));
        employee.setId(3);

        when(employeeRepository.findById("3")).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findByEmployeeId(3)).thenReturn(Collections.emptyList());
        when(salonServiceCustomerServiceRepository.findByEmployeeId(3)).thenReturn(Collections.emptyList());

        // Act
        EmployeeDeleteUseCaseOutputDto output = employeeDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(employeeRepository, times(1)).delete(employee);
    }
}