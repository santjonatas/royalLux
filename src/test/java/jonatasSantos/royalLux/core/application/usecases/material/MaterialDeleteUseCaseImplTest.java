package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaterialDeleteUseCaseImplTest {
    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialDeleteUseCaseImpl materialDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir material a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Material inexistente'")
    void deveLancarExcecaoQuandoNaoExistirMaterialASerDeletado() {
        // Arrange
        when(materialRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialDeleteUseCase.execute(1);
        });

        assertEquals("Material inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar material com sucesso e retornar true em propriedade success do output'")
    void deveDeletarMaterialComSucesso() {
        // Arrange
        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(materialRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(material));

        // Act
        MaterialDeleteUseCaseOutputDto output = materialDeleteUseCase.execute(1);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(materialRepository).delete(any(Material.class));
        verify(materialRepository, times(1)).delete(material);
    }
}