package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaterialDeleteUseCaseImplTest {
    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository           ;

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
    @DisplayName("Quando material possuir serviços vinculados, deve lançar IllegalStateException com mensagem apropriada")
    void deveLancarExcecaoQuandoMaterialPossuirServicosVinculados() {
        // Arrange
        Material material = new Material("Tinta loira", "Para descoloração", BigDecimal.valueOf(55), 8);
        material.setId(2);

        when(materialRepository.findById("2")).thenReturn(Optional.of(material));
        when(materialSalonServiceRepository.findByMaterialId(2)).thenReturn(List.of(mock(MaterialSalonService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            materialDeleteUseCase.execute(2);
        });

        assertEquals("Material não pode ser deletado pois ainda possui serviços vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando material não possuir vínculos, deve deletar com sucesso e retornar true no output")
    void deveDeletarMaterialComSucessoQuandoNaoPossuirVinculos() {
        // Arrange
        Material material = new Material("Shampoo neutro", "Uso profissional", BigDecimal.valueOf(20), 10);
        material.setId(3);

        when(materialRepository.findById("3")).thenReturn(Optional.of(material));
        when(materialSalonServiceRepository.findByMaterialId(3)).thenReturn(Collections.emptyList());

        // Act
        MaterialDeleteUseCaseOutputDto output = materialDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(materialRepository, times(1)).delete(material);
    }
}