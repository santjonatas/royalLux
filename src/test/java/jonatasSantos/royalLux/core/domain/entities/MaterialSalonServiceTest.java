package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialSalonServiceTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao serviço, estourar exceção IllegalArgumentException com mensagem 'Serviço não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoServico(){
        // Arrange
        MaterialSalonService materialSalonService = new MaterialSalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            materialSalonService.setSalonService(null);
        });

        assertEquals("Serviço não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao material, estourar exceção IllegalArgumentException com mensagem 'Material não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoMaterial(){
        // Arrange
        MaterialSalonService materialSalonService = new MaterialSalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            materialSalonService.setMaterial(null);
        });

        assertEquals("Material não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo a quantidade de materiais, estourar exceção IllegalArgumentException com mensagem 'Quantidade de materiais não pode ser nula'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAQuantidadeDeMateriais(){
        // Arrange
        MaterialSalonService materialSalonService = new MaterialSalonService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            materialSalonService.setQuantityMaterial(null);
        });

        assertEquals("Quantidade de materiais não pode ser nula", exception.getMessage());
    }
}