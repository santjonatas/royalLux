package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
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
}