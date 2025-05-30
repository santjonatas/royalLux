package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
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
import static org.mockito.Mockito.when;

class UserUpdateUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUpdateUseCaseImpl userUpdateUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.CLIENT, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir usuário a ser atualizado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário inexistente'")
    void deveLancarExcecaoQuandoUsuarioASerAtualizadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.CLIENT, true);
        User userToBeUpdated = new User(input.username(), input.role(), input.active());
        userToBeUpdated.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Usuário inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir usuário com username de usuário a ser atualizado, estourar exceção ConflictException com mensagem 'Nome de usuário já está em uso'")
    void deveLancarExcecaoQuandoJaExistirUsernameDeUsuarioASerAtualizado(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.CLIENT, true);
        User userToBeUpdated = new User(input.username(), input.role(), input.active());
        userToBeUpdated.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Nome de usuário já está em uso", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário admin tentar atualizar a si mesmo com role diferente de ADMIN, estourar exceção IllegalArgumentException com mensagem 'Admin não pode ter Permissão alterada'")
    void deveLancarExcecaoQuandoUsuarioAdminAtualizarASiMesmoComRoleDiferenteDeAdmin(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("joao_1", UserRole.CLIENT, true);
        User userToBeUpdated = userLogged;
        userToBeUpdated.setId(1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    1,
                    input
            );
        });

        assertEquals("Admin não pode ter Permissão alterada", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário admin tentar desativar a si mesmo, estourar exceção IllegalArgumentException com mensagem 'Admin não pode ter usuário inativo'")
    void deveLancarExcecaoQuandoUsuarioAdminTentarDesativarASiMesmo(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("joao_1", UserRole.ADMIN, false);
        User userToBeUpdated = userLogged;
        userToBeUpdated.setId(1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    1,
                    input
            );
        });

        assertEquals("Admin não pode ter usuário inativo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário admin tentar atualizar role de outro usuário para ADMIN, estourar exceção ConflictException com mensagem 'Apenas um usuário pode ser Admin'")
    void deveLancarExcecaoQuandoUsuarioAdminAtualizarRoleDeOutroUsuarioParaAdmin(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.ADMIN, true);
        User userToBeUpdated = new User(input.username(), UserRole.CLIENT, input.active());
        userToBeUpdated.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Apenas um usuário pode ser Admin", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário funcionário tentar atualizar qualquer usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar usuário'")
    void deveLancarExcecaoQuandoUsuarioEmployeeTentarAtualizarQualquerUsuario(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.ADMIN, true);
        User userToBeUpdated = new User(input.username(), UserRole.CLIENT, input.active());
        userToBeUpdated.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário cliente tentar atualizar outro usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar outro usuário'")
    void deveLancarExcecaoQuandoUsuarioClientTentarAtualizarOutroUsuario(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("mateus_2", UserRole.ADMIN, true);
        User userToBeUpdated = new User(input.username(), UserRole.CLIENT, input.active());
        userToBeUpdated.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário cliente tentar atualizar a própria role para ADMIN, estourar exceção ConflictException com mensagem 'Apenas um usuário pode ser Admin'")
    void deveLancarExcecaoQuandoUsuarioClientTentarAtualizarPropriaRoleParaAdmin(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);

        UserUpdateUseCaseInputDto input = new UserUpdateUseCaseInputDto("joao_1", UserRole.ADMIN, true);
        User userToBeUpdated = userLogged;
        userToBeUpdated.setId(1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(userToBeUpdated.getId())))
                .thenReturn(Optional.of(userToBeUpdated));

        when(userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userUpdateUseCase.execute(
                    userLogged,
                    1,
                    input
            );
        });

        assertEquals("Apenas um usuário pode ser Admin", exception.getMessage());
    }
}