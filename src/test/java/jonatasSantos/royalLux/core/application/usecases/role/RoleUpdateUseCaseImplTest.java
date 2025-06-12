package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseOutputDto;
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

class RoleUpdateUseCaseImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleUpdateUseCaseImpl roleUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir função a ser atualizada com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Função é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirClienteASerDeletado() {
        // Arrange
        RoleUpdateUseCaseInputDto input = new RoleUpdateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleUpdateUseCase.execute(3, input);
        });

        assertEquals("Função é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir função a ser atualizada com o mesmo nome, estourar exceção ConflictException com mensagem 'Função já existente com este nome'")
    void deveLancarExcecaoQuandoNaoExistirFuncaoASerAtualizada() {
        // Arrange
        RoleUpdateUseCaseInputDto input = new RoleUpdateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(role));

        when(roleRepository.existsByNameAndIdNot(input.name(), role.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            roleUpdateUseCase.execute(3, input);
        });

        assertEquals("Função já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar função com sucesso e retornar true em propriedade success do output")
    void deveAtualizarFuncaoComSucesso() {
        // Arrange
        RoleUpdateUseCaseInputDto input = new RoleUpdateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(role));

        when(roleRepository.existsByNameAndIdNot(input.name(), role.getId()))
                .thenReturn(false);

        // Act
        RoleUpdateUseCaseOutputDto output = roleUpdateUseCase.execute(3, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(roleRepository).save(any(Role.class));
        verify(roleRepository, times(1)).findById(String.valueOf(3));
        verify(roleRepository, times(1)).existsByNameAndIdNot(input.name(), role.getId());
    }

}