package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
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

class UserDeleteUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDeleteUseCaseImpl userDeleteUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário inexistente'")
    void deveLancarExcecaoQuandoUsuarioASerDeletadoNaoExistir() {
        // Arrange
        when(userRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userDeleteUseCase.execute(
                    2
            );
        });

        assertEquals("Usuário inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando role de usuário a ser deletado for ADMIN, estourar exceção UnauthorizedException com mensagem 'Admin não pode ser deletado'")
    void deveLancarExcecaoQuandoUsuarioASerDeletadoForAdmin() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);

        when(userRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(userLogged));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            userDeleteUseCase.execute(
                    1
            );
        });

        assertEquals("Admin não pode ser deletado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso e retornar true em propriedade success do output")
    void deveDeletarUsuarioComSucesso(){
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        when(userRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(user));

        // Act
        UserDeleteUseCaseOutputDto output = userDeleteUseCase.execute(2);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(userRepository).delete(any(User.class));
        verify(userRepository, times(1)).delete(user);
    }
}