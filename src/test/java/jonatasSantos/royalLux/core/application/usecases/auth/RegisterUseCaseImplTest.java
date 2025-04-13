package jonatasSantos.royalLux.core.application.usecases.auth;

import jakarta.persistence.EntityExistsException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseOutputDto;
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

class RegisterUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUseCaseImpl registerUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando já existir usuário com o mesmo username, estourar exceção EntityExistsException com mensagem 'Usuário já existe'")
    void deveLancarExcecaoQuandoUsernameJaExistir() {
        // Arrange
        var input = new RegisterUseCaseInputDto("joao_1", "Teste123@");
        User existingUser = new User("joao_1", UserRole.CLIENT, true);

        when(userRepository.findByUsername(input.username()))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            registerUseCase.execute(input);
        });

        assertEquals("Usuário já existe", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar id de usuário cadastrado")
    void deveRealizarExecucaoComSucesso() {
        // Arrange
        var input = new RegisterUseCaseInputDto("joao_1", "Teste123@");

        when(userRepository.findByUsername(input.username()))
                .thenReturn(Optional.empty());

        User user = new User(input.username(), UserRole.CLIENT, true);
        user.validatePassword(input.password());
        user.setPassword(passwordEncoder.encode(input.password()));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userCreated = invocation.getArgument(0);
            userCreated.setId(10);
            return userCreated;
        });

        // Act
        RegisterUseCaseOutputDto output = registerUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(10, output.userId());
        verify(userRepository).save(any(User.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
}
