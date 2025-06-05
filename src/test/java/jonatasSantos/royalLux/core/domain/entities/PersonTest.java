package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao user, estourar exceção IllegalArgumentException com mensagem 'Usuário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUser(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setUser(null);
        });

        assertEquals("Usuário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoNome(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setName(null);
        });

        assertEquals("Nome não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não pode ser vazio'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAoNome(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setName("");
        });

        assertEquals("Nome não pode ser vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 3 caracteres ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome deve conter pelo menos 3 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDe3CaracteresAoNome(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setName("Lu");
        });

        assertEquals("Nome deve conter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 255 caracteres ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não deve conter mais que 255 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe255CaracteresAoNome(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setName("Nomes excessivamente longos dificultam memorização, escrita e pronúncia, além de comprometer a identidade visual e a comunicação. Eles podem gerar confusão, ser pouco atrativos e difíceis de divulgar. Nomes curtos transmitem clareza, profissionalismo e são mais impactantes.\n");
        });

        assertEquals("Nome não deve conter mais que 255 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com caracteres inválidos ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome deve conter apenas letras e espaços'")
    void deveLancarExcecaoQuandoAtribuirValorComCaracteresInvalidosAoNome(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setName("João 10");
        });

        assertEquals("Nome deve conter apenas letras e espaços", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com data no futuro a data de nascimento, estourar exceção IllegalArgumentException com mensagem 'Data de nascimento não pode ser no futuro'")
    void deveLancarExcecaoQuandoAtribuirValorComDataFuturaADataDeNascimento(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setDateBirth(LocalDate.now().plusDays(1));
        });

        assertEquals("Data de nascimento não pode ser no futuro", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com data de nascimento maior que 120 anos, estourar exceção IllegalArgumentException com mensagem 'Data de nascimento inválida. Idade não pode ser superior a 120 anos'")
    void deveLancarExcecaoQuandoAtribuirValorComDataDeNascimentoMaiorQue120Anos(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setDateBirth(LocalDate.now().minusYears(121));
        });

        assertEquals("Data de nascimento inválida. Idade não pode ser superior a 120 anos", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao cpf, estourar exceção IllegalArgumentException com mensagem 'CPF não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoCpf(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setCpf(null);
        });

        assertEquals("CPF não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor inválido ao cpf, estourar exceção IllegalArgumentException com mensagem 'CPF inválido'")
    void deveLancarExcecaoQuandoAtribuirValorInvalidoAoCpf(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setCpf("12345678910");
        });

        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com quantidade de caracteres diferente de 11 a telefone, estourar exceção IllegalArgumentException com mensagem 'Telefone deve conter 11 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorComQuantidadeDeCaracteresDiferenteDe11Atelefone(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setPhone("1199893651");
        });

        assertEquals("Telefone deve conter 11 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao email, estourar exceção IllegalArgumentException com mensagem 'Email não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoEmail(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setEmail(null);
        });

        assertEquals("Email não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 255 caracteres ao email, estourar exceção IllegalArgumentException com mensagem 'Email não deve conter mais que 255 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe255CaracteresAoEmail(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setEmail("Emails excessivamente longos dificultam memorização, escrita e pronúncia, além de comprometer a identidade visual e a comunicação. Eles podem gerar confusão, ser pouco atrativos e difíceis de divulgar. Emails curtos transmitem clareza, profissionalismo e são mais impactantes.\n");
        });

        assertEquals("Email não deve conter mais que 255 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor inválido ao email, estourar exceção IllegalArgumentException com mensagem 'Email inválido'")
    void deveLancarExcecaoQuandoAtribuirValorInvalidoAoEmail(){
        // Arrange
        Person person = new Person();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            person.setEmail("testandoemail@gmail");
        });

        assertEquals("Email inválido", exception.getMessage());
    }


}