package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientDeleteUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

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
    @DisplayName("Quando cliente possuir atendimentos vinculados, deve lançar IllegalStateException com mensagem apropriada")
    void deveLancarExcecaoQuandoClientePossuirAtendimentosVinculados() {
        // Arrange
        User user = new User("joana_cli", UserRole.CLIENT, true);
        Client client = new Client(user);
        client.setId(7);

        when(clientRepository.findById("7")).thenReturn(Optional.of(client));
        when(customerServiceRepository.findByClientId(7))
                .thenReturn(List.of(mock(CustomerService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            clientDeleteUseCase.execute(7);
        });

        assertEquals("Cliente não pode ser deletado pois ainda possui atendimentos vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando cliente não possuir vínculos, deve deletar com sucesso e retornar true no output")
    void deveDeletarClienteComSucessoQuandoNaoPossuirVinculos() {
        // Arrange
        User user = new User("lucas_cli", UserRole.CLIENT, true);
        Client client = new Client(user);
        client.setId(8);

        when(clientRepository.findById("8")).thenReturn(Optional.of(client));
        when(customerServiceRepository.findByClientId(8)).thenReturn(Collections.emptyList());

        // Act
        ClientDeleteUseCaseOutputDto output = clientDeleteUseCase.execute(8);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(clientRepository, times(1)).delete(client);
    }
}