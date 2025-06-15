package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceCreateUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerServiceCreateUseCaseImpl customerServiceCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        CustomerServiceCreateUseCaseInputDto input = new CustomerServiceCreateUseCaseInputDto(1, CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir cliente com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Cliente é inexistente'")
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        CustomerServiceCreateUseCaseInputDto input = new CustomerServiceCreateUseCaseInputDto(1, CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Client client = new Client(user);
        client.setId(1);

        when(clientRepository.findById(String.valueOf(input.clientId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Cliente é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar atendimento com sucesso e retornar id de atendimento cadastrado")
    void deveCriarAtendimentoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        CustomerServiceCreateUseCaseInputDto input = new CustomerServiceCreateUseCaseInputDto(1, CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Client client = new Client(user);
        client.setId(1);

        when(clientRepository.findById(String.valueOf(input.clientId())))
                .thenReturn(Optional.of(client));

        when(customerServiceRepository.save(any(CustomerService.class))).thenAnswer(invocation -> {
            CustomerService customerServiceCreated = invocation.getArgument(0);
            customerServiceCreated.setId(1);
            return customerServiceCreated;
        });

        // Act
        CustomerServiceCreateUseCaseOutputDto output = customerServiceCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.customerServiceId());
        verify(customerServiceRepository).save(any(CustomerService.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(clientRepository, times(1)).findById(any(String.class));
    }
}