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

    @Test
    @DisplayName("Quando atribuir valor nulo a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha não pode ser nula'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword(null);
        });

        assertEquals("Senha não pode ser nula", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha não pode ser vazia'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("");
        });

        assertEquals("Senha não pode ser vazia", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 8 caracteres a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha deve conter pelo menos 8 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDeOitoCaracteresAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("test123");
        });

        assertEquals("Senha deve conter pelo menos 8 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDeCinquentaCaracteresAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("Senha segura protege seus dados contra roubos e invasões");
        });

        assertEquals("Senha não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor sem letra maiúscula a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha deve conter pelo menos uma letra maiúscula'")
    void deveLancarExcecaoQuandoAtribuirValorSemLetraMaiusculaAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("test123@");
        });

        assertEquals("Senha deve conter pelo menos uma letra maiúscula", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor sem letra minúscula a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha deve conter pelo menos uma letra minúscula'")
    void deveLancarExcecaoQuandoAtribuirValorSemLetraMinusculaAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("TEST123@");
        });

        assertEquals("Senha deve conter pelo menos uma letra minúscula", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor sem número a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha deve conter pelo menos um número'")
    void deveLancarExcecaoQuandoAtribuirValorSemNumeroAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("TestAbc@");
        });

        assertEquals("Senha deve conter pelo menos um número", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor sem caractere especial a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha deve conter pelo menos um caractere especial'")
    void deveLancarExcecaoQuandoAtribuirValorSemCaractereEspecialAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("Test1234");
        });

        assertEquals("Senha deve conter pelo menos um caractere especial", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com caractere especial inválido a validação de senha, estourar exceção IllegalArgumentException com mensagem 'Senha contém caracteres inválidos'")
    void deveLancarExcecaoQuandoAtribuirValorComCaractereEspecialInvalidoAValidacaoDeSenha(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.validatePassword("Test123@ ");
        });

        assertEquals("Senha contém caracteres inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Atribuir valor com sucesso a validação de senha e retornar true")
    void deveAtribuirValorAValidacaoDeSenhaComSucesso(){
        // Arrange
        User user = new User();

        // Act
        Boolean validatePassword = user.validatePassword("Test123@");

        // Assert
        assertNotNull(validatePassword);
        assertEquals(true, validatePassword);
    }

    @Test
    @DisplayName("Quando atribuir valor nulo a role, estourar exceção IllegalArgumentException com mensagem 'Permissão não pode ser nula'")
    void deveLancarExcecaoQuandoAtribuirValorNuloARole(){
        // Arrange
        User user = new User();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setRole(null);
        });

        assertEquals("Permissão não pode ser nula", exception.getMessage());
    }
}