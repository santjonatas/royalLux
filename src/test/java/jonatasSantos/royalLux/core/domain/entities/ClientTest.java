package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao user, estourar exceção IllegalArgumentException com mensagem 'Usuário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUser(){
        // Arrange
        Client client = new Client();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setUser(null);
        });

        assertEquals("Usuário não pode ser nulo", exception.getMessage());
    }
}