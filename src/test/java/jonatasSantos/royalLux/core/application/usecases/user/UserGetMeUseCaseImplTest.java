package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
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

class UserGetMeUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGetMeUseCaseImpl userGetMeUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário inexistente'")
    void execute() {
        // Arrange
        User userLooged = new User("joao_1", UserRole.CLIENT, true);

        when(userRepository.findById(String.valueOf(userLooged.getId()))).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userGetMeUseCase.execute(userLooged);
        });

        assertEquals("Usuário inexistente", exception.getMessage());
    }
}