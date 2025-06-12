package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRoleTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao funcionário, estourar exceção IllegalArgumentException com mensagem 'Funcionário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoFuncionario(){
        // Arrange
        EmployeeRole employeeRole = new EmployeeRole();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeRole.setEmployee(null);
        });

        assertEquals("Funcionário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo a função, estourar exceção IllegalArgumentException com mensagem 'Função não pode ser nula'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAFuncao(){
        // Arrange
        EmployeeRole employeeRole = new EmployeeRole();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeRole.setRole(null);
        });

        assertEquals("Função não pode ser nula", exception.getMessage());
    }
}