package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    @Test
    @DisplayName("Quando atribuir valor vazio ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do cargo não pode ser vazio'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAoNome(){
        // Arrange
        Role role = new Role();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            role.setName("");
        });

        assertEquals("Nome do cargo não pode ser vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do cargo não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe50CaracteresAoNome(){
        // Arrange
        Role role = new Role();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            role.setName("Teste aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });

        assertEquals("Nome do cargo não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 3000 caracteres a detalhes, estourar exceção IllegalArgumentException com mensagem 'Detalhes não deve conter mais que 3000 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe3000CaracteresADetalhes(){
        // Arrange
        Role role = new Role();

        StringBuilder detailBuilder = new StringBuilder();
        for (int i = 0; i < 3005; i++) {
            detailBuilder.append(i);
        }
        String detail = detailBuilder.toString();

        // Act + Assert
        String finalDetail = detail;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            role.setDetail(finalDetail);
        });

        assertEquals("Detalhes não deve conter mais que 3000 caracteres", exception.getMessage());
    }
}