package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao username, estourar exceção IllegalArgumentException com mensagem 'Username não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername(null);
        });

        assertEquals("Username não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio ao username, estourar exceção IllegalArgumentException com mensagem 'Username não pode ser vazio'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("");
        });

        assertEquals("Username não pode ser vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 5 caracteres ao username, estourar exceção IllegalArgumentException com mensagem 'Username deve conter pelo menos 5 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDeCincoCaracteresAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("joao");
        });

        assertEquals("Username deve conter pelo menos 5 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 25 caracteres ao username, estourar exceção IllegalArgumentException com mensagem 'Username não deve conter mais que 25 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDeVinteECincoCaracteresAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("joaotestandousernamenovo12345");
        });

        assertEquals("Username não deve conter mais que 25 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com letra maiúscula ao username, estourar exceção IllegalArgumentException com mensagem 'O nome de usuário só pode conter letras minúsculas, números, '.' e '_''")
    void deveLancarExcecaoQuandoAtribuirValorComLetraMaiusculaAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("Joao_123");
        });

        assertEquals("O nome de usuário só pode conter letras minúsculas, números, '.' e '_'", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com caracteres especiais inválidos ao username, estourar exceção IllegalArgumentException com mensagem 'O nome de usuário só pode conter letras minúsculas, números, '.' e '_''")
    void deveLancarExcecaoQuandoAtribuirValorComCaracteresEspeciaisInvalidosAoUsername(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("joao_123@");
        });

        assertEquals("O nome de usuário só pode conter letras minúsculas, números, '.' e '_'", exception.getMessage());
    }
}