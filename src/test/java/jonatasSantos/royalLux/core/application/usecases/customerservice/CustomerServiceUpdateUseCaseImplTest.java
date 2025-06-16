package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseOutputDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceUpdateUseCaseImplTest {

    @Mock
    private CustomerServiceRepository customerServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerServiceUpdateUseCaseImpl customerServiceUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        CustomerServiceUpdateUseCaseInputDto input = new CustomerServiceUpdateUseCaseInputDto(CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir atendimento a ser atualizado, estourar exceção EntityNotFoundException com mensagem 'Atendimento é inexistente'")
    void deveLancarExcecaoQuandoAtendimentoASerAtualizadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        CustomerServiceUpdateUseCaseInputDto input = new CustomerServiceUpdateUseCaseInputDto(CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(customerServiceRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Atendimento é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for funcionário e atendimento a ser atualizado já tiver sido finalizado, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar um atendimento que já foi finalizado'")
    void deveLancarExcecaoQuandoUsuarioForFuncionarioEAtendimentoASerAtualizadoJaTiverSidoFinalizado() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);
        CustomerServiceUpdateUseCaseInputDto input = new CustomerServiceUpdateUseCaseInputDto(CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );

        when(customerServiceRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(customerService));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            customerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar um atendimento que já foi finalizado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar atendimento com sucesso e retornar true em propriedade success do output")
    void deveAtualizarAtendimentoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);
        CustomerServiceUpdateUseCaseInputDto input = new CustomerServiceUpdateUseCaseInputDto(CustomerServiceStatus.AGENDADO, LocalDateTime.now(), "");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User user = new User("mateus_2", UserRole.CLIENT, true);
        Client client = new Client(user);

        User user2 = new User("marcos_2", UserRole.EMPLOYEE, true);

        CustomerService customerService = new CustomerService(
                user2,
                client,
                CustomerServiceStatus.FINALIZADO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(40),
                ""
        );

        when(customerServiceRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(customerService));

        // Act
        CustomerServiceUpdateUseCaseOutputDto output = customerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(customerServiceRepository).save(any(CustomerService.class));
        verify(userRepository, times(1)).findById(String.valueOf(1));
        verify(customerServiceRepository, times(1)).findById(String.valueOf(2));
    }
}