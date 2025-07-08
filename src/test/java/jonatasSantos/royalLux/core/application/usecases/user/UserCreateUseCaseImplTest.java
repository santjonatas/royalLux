package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityExistsException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserCreateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCreateUseCaseImpl userCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando já existir usuário com o mesmo username, estourar exceção EntityExistsException com mensagem 'Usuário já existe'")
    void deveLancarExcecaoQuandoUsernameJaExistir() {
        //
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        var input = new UserCreateUseCaseInputDto("joao_1", "Teste123@", UserRole.ADMIN, true);
        User existingUser = new User("joao_1", UserRole.CLIENT, true);

        when(userRepository.findByUsername(input.username()))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            userCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário já existe", exception.getMessage());
    }

    @Test
    @DisplayName("Quando role passada como input pelo usuário for ADMIN, estourar exceção IllegalArgumentException com mensagem 'Apenas um usuário pode ser Admin'")
    void deveLancarExcecaoQuandoRoleForAdmin(){
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        var input = new UserCreateUseCaseInputDto("joao_1", "Teste123@", UserRole.ADMIN, true);

        when(userRepository.findByUsername(input.username())).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Apenas um usuário pode ser Admin", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso e retornar id de usuário cadastrado")
    void deveCriarUsuarioComSucesso(){
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        var input = new UserCreateUseCaseInputDto("joao_1", "Teste123@", UserRole.EMPLOYEE, true);

        when(userRepository.findByUsername(input.username())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userCreated = invocation.getArgument(0);
            userCreated.setId(10);
            return userCreated;
        });

        // Act
        UserCreateUseCaseOutputDto output = userCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(10, output.userId());
        verify(userRepository).save(any(User.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
}