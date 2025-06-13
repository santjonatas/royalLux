package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
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

class MaterialCreateUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MaterialCreateUseCaseImpl materialCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialCreateUseCaseInputDto input = new MaterialCreateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir material com mesmo nome, estourar exceção ConflictException com mensagem 'Material já existente com este nome'")
    void deveLancarExcecaoQuandoMaterialJaExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialCreateUseCaseInputDto input = new MaterialCreateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(materialRepository.existsByName(input.name()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            materialCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Material já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar material com sucesso e retornar id de material cadastrado")
    void deveCriarMaterialComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialCreateUseCaseInputDto input = new MaterialCreateUseCaseInputDto("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(materialRepository.existsByName(input.name()))
                .thenReturn(false);

        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> {
            Material materialCreated = invocation.getArgument(0);
            materialCreated.setId(1);
            return materialCreated;
        });

        // Act
        MaterialCreateUseCaseOutputDto output = materialCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.materialId());
        verify(materialRepository).save(any(Material.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(materialRepository, times(1)).existsByName(any(String.class));
    }
}