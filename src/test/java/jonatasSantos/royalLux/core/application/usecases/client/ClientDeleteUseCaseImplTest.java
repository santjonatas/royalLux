package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
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

class ClientDeleteUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientDeleteUseCaseImpl clientDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir cliente a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Cliente inexistente'")
    void deveLancarExcecaoQuandoNaoExistirClienteASerDeletado() {
        // Arrange
        when(clientRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientDeleteUseCase.execute(3);
        });

        assertEquals("Cliente inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso e retornar true em propriedade success do output")
    void deveDeletarClienteComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        when(clientRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(client));

        // Act
        ClientDeleteUseCaseOutputDto output = clientDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(clientRepository).delete(any(Client.class));
        verify(clientRepository, times(1)).delete(client);
    }
}