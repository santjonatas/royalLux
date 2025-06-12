package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoNome(){
        // Arrange
        Role role = new Role();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            role.setName(null);
        });

        assertEquals("Nome não pode ser nulo", exception.getMessage());
    }


}