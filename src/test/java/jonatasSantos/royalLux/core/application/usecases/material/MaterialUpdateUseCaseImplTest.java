package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.Role;
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

class MaterialUpdateUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialUpdateUseCaseImpl materialUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir material a ser atualizado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Material é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirMaterialASerAtualizado() {
        // Arrange
        MaterialUpdateUseCaseInputDto input = new MaterialUpdateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40));

        when(materialRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialUpdateUseCase.execute(3, input);
        });

        assertEquals("Material é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir material com o mesmo nome, estourar exceção ConflictException com mensagem 'Material já existente com este nome'")
    void deveLancarExcecaoQuandoJaExistirMaterialComOMesmoNome() {
        // Arrange
        MaterialUpdateUseCaseInputDto input = new MaterialUpdateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40));
        Material material = new Material(input.name(), input.description(), input.value(), 5);
        material.setId(3);

        when(materialRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(material));

        when(materialRepository.existsByNameAndIdNot(input.name(), 3))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            materialUpdateUseCase.execute(3, input);
        });

        assertEquals("Material já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar material com sucesso e retornar true em propriedade success do output")
    void deveAtualizarMaterialComSucesso() {
        // Arrange
        MaterialUpdateUseCaseInputDto input = new MaterialUpdateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40));
        Material material = new Material(input.name(), input.description(), input.value(), 5);
        material.setId(3);

        when(materialRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(material));

        when(materialRepository.existsByNameAndIdNot(input.name(), 3))
                .thenReturn(false);

        // Act
        MaterialUpdateUseCaseOutputDto output = materialUpdateUseCase.execute(3, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(materialRepository).save(any(Material.class));
        verify(materialRepository, times(1)).findById(String.valueOf(3));
        verify(materialRepository, times(1)).existsByNameAndIdNot(input.name(), 3);
    }
}