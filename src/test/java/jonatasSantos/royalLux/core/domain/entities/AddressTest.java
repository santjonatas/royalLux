package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao user, estourar exceção IllegalArgumentException com mensagem 'Usuário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUser(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setUser(null);
        });

        assertEquals("Usuário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 2 caracteres a rua, estourar exceção IllegalArgumentException com mensagem 'Rua deve conter pelo menos 2 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDe2CaracteresARua(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setStreet("A");
        });

        assertEquals("Rua deve conter pelo menos 2 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 255 caracteres a rua, estourar exceção IllegalArgumentException com mensagem 'Rua não deve conter mais que 255 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe2CaracteresARua(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setStreet("Nomes de ruas excessivamente longos dificultam memorização, escrita e pronúncia, além de comprometer a identidade visual e a comunicação. Eles podem gerar confusão, ser pouco atrativos e difíceis de divulgar. Ruas com nomes curtos transmitem clareza, profissionalismo e são mais impactantes.");
        });

        assertEquals("Rua não deve conter mais que 255 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio a número de casa, estourar exceção IllegalArgumentException com mensagem 'Número deve conter pelo menos 1 caractere'")
    void deveLancarExcecaoQuandoAtribuirValorVazioANumeroDaCasa(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setHouseNumber("");
        });

        assertEquals("Número deve conter pelo menos 1 caractere", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 15 caracteres a número de casa, estourar exceção IllegalArgumentException com mensagem 'Número não deve conter mais que 15 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe15CaracteresANumeroDaCasa(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setHouseNumber("12345678910111213");
        });

        assertEquals("Número não deve conter mais que 15 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres a complemento, estourar exceção IllegalArgumentException com mensagem 'Complemento não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe50CaracteresAComplemento(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setComplement("Complemento de endereço: detalhes extras da localização.");
        });

        assertEquals("Complemento não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 2 caracteres ao bairro, estourar exceção IllegalArgumentException com mensagem 'Bairro deve conter pelo menos 2 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDe2CaracteresAoBairro(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setNeighborhood("A");
        });

        assertEquals("Bairro deve conter pelo menos 2 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 100 caracteres ao bairro, estourar exceção IllegalArgumentException com mensagem 'Bairro não deve conter mais que 100 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe100CaracteresAoBairro(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setNeighborhood("Bairro é a divisão de uma cidade que ajuda a localizar endereços e identificar regiões com características comuns.");
        });

        assertEquals("Bairro não deve conter mais que 100 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 2 caracteres a cidade, estourar exceção IllegalArgumentException com mensagem 'Cidade deve conter pelo menos 2 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDe2CaracteresACidade(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setCity("A");
        });

        assertEquals("Cidade deve conter pelo menos 2 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 100 caracteres a cidade, estourar exceção IllegalArgumentException com mensagem 'Cidade não deve conter mais que 100 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe100CaracteresACidade(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setCity("Cidade é um centro urbano com infraestrutura, serviços e população, onde ocorrem atividades sociais e econômicas.");
        });

        assertEquals("Cidade não deve conter mais que 100 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor quantidade de caracteres diferente de 9 a CEP, estourar exceção IllegalArgumentException com mensagem 'CEP deve conter 9 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorComQuantidadeDeCaracteresDiferenteDe9AoCEP(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setCep("12345678");
        });

        assertEquals("CEP deve conter 9 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor inválido ao CEP, estourar exceção IllegalArgumentException com mensagem 'CEP com formato inválido'")
    void deveLancarExcecaoQuandoAtribuirValorComFormatoInvalidoAoCEP(){
        // Arrange
        Address address = new Address();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setCep("123ab456c");
        });

        assertEquals("CEP com formato inválido", exception.getMessage());
    }
}