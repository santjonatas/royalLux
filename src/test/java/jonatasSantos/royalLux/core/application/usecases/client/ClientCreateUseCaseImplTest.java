package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientCreateUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientCreateUseCaseImpl clientCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir usuário de cliente a ser criado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioDeClienteASerCriadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando role de usuário de cliente a ser criado não for CLIENT, estourar exceção IllegalArgumentException com mensagem 'Usuário deve ser um cliente'")
    void deveLancarExcecaoQuandoRoleDeUsuarioDeClienteASerCriadoNaoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        User client = new User("mateus_2", UserRole.EMPLOYEE, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(client));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário deve ser um cliente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente e tentar criar clienta para outro usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para criar outro cliente'")
    void deveLancarExcecaoQuandoUsuarioLogadoForClienteERTentarCriarClienteParaOutroUsuario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        User client = new User("mateus_2", UserRole.CLIENT, true);
        client.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(client));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            clientCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Você não possui autorização para criar outro cliente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário já estiver vinculado a um cliente, estourar exceção ConflictException com mensagem 'Cliente já vinculado a um usuário'")
    void deveLancarExcecaoQuandoUsuarioJaEstiverVinculadoAUmCliente() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        User client = new User("mateus_2", UserRole.CLIENT, true);
        client.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(client));

        when(clientRepository.existsByUserId(client.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            clientCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Cliente já vinculado a um usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso e retornar id de cliente cadastrado")
    void deveCriarClienteComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);
        ClientCreateUseCaseInputDto input = new ClientCreateUseCaseInputDto(2);

        User client = new User("mateus_2", UserRole.CLIENT, true);
        client.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(client));

        when(clientRepository.existsByUserId(client.getId()))
                .thenReturn(false);

        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client clientCreated = invocation.getArgument(0);
            clientCreated.setId(2);
            return clientCreated;
        });

        // Act
        ClientCreateUseCaseOutputDto output = clientCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(2, output.clientId());
        verify(clientRepository).save(any(Client.class));
        verify(userRepository, times(2)).findById(any(String.class));
        verify(clientRepository, times(1)).existsByUserId(any(Integer.class));
    }
}