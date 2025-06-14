package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalonServiceTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoNome(){
        // Arrange
        SalonService salonService = new SalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setName(null);
        });

        assertEquals("Nome não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor vazio ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do serviço não pode ser vazio'")
    void deveLancarExcecaoQuandoAtribuirValorVazioAoNome(){
        // Arrange
        SalonService salonService = new SalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setName("");
        });

        assertEquals("Nome do serviço não pode ser vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres ao nome, estourar exceção IllegalArgumentException com mensagem 'Nome do serviço não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe50CaracteresAoNome(){
        // Arrange
        SalonService salonService = new SalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setName("Teste aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });

        assertEquals("Nome do serviço não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 1000 caracteres a descrição, estourar exceção IllegalArgumentException com mensagem 'Descrição não deve conter mais que 1000 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe1000CaracteresADescricao(){
        // Arrange
        SalonService salonService = new SalonService();

        StringBuilder detailBuilder = new StringBuilder();
        for (int i = 0; i < 1005; i++) {
            detailBuilder.append(i);
        }
        String detail = detailBuilder.toString();

        // Act + Assert
        String finalDetail = detail;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setDescription(finalDetail);
        });

        assertEquals("Descrição não deve conter mais que 1000 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao tempo estimado, estourar exceção IllegalArgumentException com mensagem 'Tempo estimado não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoTempoEstimado(){
        // Arrange
        SalonService salonService = new SalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setEstimatedTime(null);
        });

        assertEquals("Tempo estimado não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao valor, estourar exceção IllegalArgumentException com mensagem 'Valor não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoValor(){
        // Arrange
        SalonService salonService = new SalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonService.setValue(null);
        });

        assertEquals("Valor não pode ser nulo", exception.getMessage());
    }
}