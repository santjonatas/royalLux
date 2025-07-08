package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
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

class MaterialDecrementQuantityUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialDecrementQuantityUseCaseImpl materialDecrementQuantityUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir material a ser decrementado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Material é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirMaterialASerDecrementado() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        MaterialDecrementQuantityUseCaseInputDto input = new MaterialDecrementQuantityUseCaseInputDto(3);

        when(materialRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialDecrementQuantityUseCase.execute(userLogged, 1, input);
        });

        assertEquals("Material é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve decrementar material com sucesso e retornar quantidade atual em propriedade currentQuantity do output'")
    void deveDecrementarMaterialComSucesso() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        MaterialDecrementQuantityUseCaseInputDto input = new MaterialDecrementQuantityUseCaseInputDto(3);

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(materialRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(material));

        // Act
        MaterialDecrementQuantityUseCaseOutputDto output = materialDecrementQuantityUseCase.execute(userLogged, 1, input);

        // Assert
        assertNotNull(output);
        assertEquals(2, output.currentQuantity());
        verify(materialRepository).save(any(Material.class));
        verify(materialRepository, times(1)).findById(any(String.class));
        verify(materialRepository, times(1)).save(any(Material.class));
    }
}