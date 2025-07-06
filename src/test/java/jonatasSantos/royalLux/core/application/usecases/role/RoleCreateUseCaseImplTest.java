package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Role;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
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

class RoleCreateUseCaseImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleCreateUseCaseImpl roleCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        RoleCreateUseCaseInputDto input = new RoleCreateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir função com o mesmo nome, estourar exceção ConflictException com mensagem 'Função já existente com este nome'")
    void deveLancarExcecaoQuandoJaExistirFuncaoComMesmoNome() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        RoleCreateUseCaseInputDto input = new RoleCreateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(roleRepository.existsByName(input.name()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            roleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Função já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar função com sucesso e retornar id de função cadastrada")
    void deveCriarFuncaoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        RoleCreateUseCaseInputDto input = new RoleCreateUseCaseInputDto("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(roleRepository.existsByName(input.name()))
                .thenReturn(false);

        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role roleCreated = invocation.getArgument(0);
            roleCreated.setId(2);
            return roleCreated;
        });

        // Act
        RoleCreateUseCaseOutputDto output = roleCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(2, output.roleId());
        verify(roleRepository).save(any(Role.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(roleRepository, times(1)).existsByName(any(String.class));
    }
}