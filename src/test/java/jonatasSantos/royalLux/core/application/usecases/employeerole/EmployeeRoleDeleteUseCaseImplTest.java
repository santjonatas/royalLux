package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
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

class EmployeeRoleDeleteUseCaseImplTest {

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @InjectMocks
    private EmployeeRoleDeleteUseCaseImpl employeeRoleDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir vínculo entre funcionário e função a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Vínculo entre funcionário e função inexistente'")
    void deveLancarExcecaoQuandoNaoExistirVinculoEntreFuncionarioEFuncaoASerDeletado() {
        // Arrange
        when(employeeRoleRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeRoleDeleteUseCase.execute(1);
        });

        assertEquals("Vínculo entre funcionário e função inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar vínculo entre funcionário e função com sucesso e retornar true em propriedade success do output")
    void deveDeletarVinculoEntreFuncionarioEFuncaoComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        Employee employee = new Employee(user, "Barbeiro", BigDecimal.valueOf(2000));
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");
        EmployeeRole employeeRole = new EmployeeRole(employee, role);

        when(employeeRoleRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(employeeRole));

        // Act
        EmployeeRoleDeleteUseCaseOutputDto output = employeeRoleDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(employeeRoleRepository).delete(any(EmployeeRole.class));
        verify(employeeRoleRepository, times(1)).delete(employeeRole);
    }
}