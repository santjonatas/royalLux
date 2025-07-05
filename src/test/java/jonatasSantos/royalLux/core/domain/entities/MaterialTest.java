package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoNome(){
        // Arrange
        Material material = new Material();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.setName(null);
        });

        assertEquals("Nome não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do material não pode ser vazio'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAoNome(){
        // Arrange
        Material material = new Material();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.setName("");
        });

        assertEquals("Nome do material não pode ser vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do material não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe50CaracteresAoNome(){
        // Arrange
        Material material = new Material();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.setName("Teste aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });

        assertEquals("Nome do material não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 3000 caracteres a descrição, estourar exceção IllegalArgumentException com mensagem 'Descrição não deve conter mais que 3000 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe3000CaracteresADescricao(){
        // Arrange
        Material material = new Material();

        StringBuilder detailBuilder = new StringBuilder();
        for (int i = 0; i < 3005; i++) {
            detailBuilder.append(i);
        }
        String detail = detailBuilder.toString();

        // Act + Assert
        String finalDetail = detail;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.setDescription(finalDetail);
        });

        assertEquals("Descrição não deve conter mais que 3000 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo a quantidade, estourar exceção IllegalArgumentException com mensagem 'Quantidade disponível não pode ser nula'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAQuantidade(){
        // Arrange
        Material material = new Material();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.setAvailableQuantity(null);
        });

        assertEquals("Quantidade disponível não pode ser nula", exception.getMessage());
    }

    @Test
    @DisplayName("Quando decrementar um valor maior que quantidade, estourar exceção IllegalArgumentException com mensagem 'Quantidade a ser removida não pode ser maior que a atual'")
    void deveLancarExcecaoQuandoDecrementarUmValorMaiorQueQuantidade(){
        // Arrange
        Material material = new Material();
        material.setAvailableQuantity(5);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            material.decrementQuantity(10);
        });

        assertEquals("Quantidade a ser removida não pode ser maior que a atual", exception.getMessage());
    }
}