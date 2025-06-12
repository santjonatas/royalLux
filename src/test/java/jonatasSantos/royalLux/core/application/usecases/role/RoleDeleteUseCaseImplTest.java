package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleDeleteUseCaseImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleDeleteUseCaseImpl roleDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir função a ser deletada com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Função inexistente'")
    void deveLancarExcecaoQuandoNaoExistirFuncaoASerDeletada() {
        // Arrange
        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleDeleteUseCase.execute(3);
        });

        assertEquals("Função inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar função com sucesso e retornar true em propriedade success do output")
    void deveDeletarFuncaoComSucesso() {
        // Arrange
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(role));

        // Act
        RoleDeleteUseCaseOutputDto output = roleDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(roleRepository).delete(any(Role.class));
        verify(roleRepository, times(1)).delete(role);
    }
}