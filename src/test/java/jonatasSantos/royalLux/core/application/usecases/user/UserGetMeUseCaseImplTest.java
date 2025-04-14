package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetMeUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        when(userRepository.findById(String.valueOf(userLogged.getId()))).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userGetMeUseCase.execute(userLogged);
        });

        assertEquals("Usuário inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar usuário com sucesso")
    void deveBuscarUsuarioComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(10);
        userLogged.setUpdatedAt(LocalDateTime.of(2025, 4, 10, 12, 0));

        when(userRepository.findById(String.valueOf(userLogged.getId()))).thenReturn(Optional.of(userLogged));

        // Act
        UserGetMeUseCaseOutputDto output = userGetMeUseCase.execute(userLogged);

        // Assert
        assertEquals(userLogged.getId(), output.id());
        assertEquals(userLogged.getUsername(), output.username());
        assertEquals(userLogged.getRole(), output.role());
        assertEquals(userLogged.isActive(), output.active());
        assertEquals(userLogged.getCreatedAt(), output.createdAt());
        assertEquals(userLogged.getUpdatedAt(), output.updatedAt());
    }
}