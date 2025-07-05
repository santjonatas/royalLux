package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
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

class UserDeleteUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    CustomerServiceRepository customerServiceRepository;

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
    @DisplayName("Quando usuário possuir dados pessoais vinculados, deve lançar IllegalStateException com mensagem específica")
    void deveLancarExcecaoQuandoUsuarioPossuirDadosPessoais() {
        // Arrange
        User user = new User("marcos_2", UserRole.CLIENT, true);
        user.setId(4);

        when(userRepository.findById("4")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(4)).thenReturn(mock(Person.class));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(4);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui dados pessoais vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário possuir endereço vinculado, deve lançar IllegalStateException com mensagem específica")
    void deveLancarExcecaoQuandoUsuarioPossuirEndereco() {
        // Arrange
        User user = new User("bruna_2", UserRole.CLIENT, true);
        user.setId(5);

        when(userRepository.findById("5")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(5)).thenReturn(null);
        when(addressRepository.findByUserId(5)).thenReturn(List.of(mock(Address.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(5);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui endereço vinculado", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário for funcionário e possuir dados vinculados, deve lançar IllegalStateException")
    void deveLancarExcecaoQuandoUsuarioFuncionarioPossuirDados() {
        // Arrange
        User user = new User("lucas_func", UserRole.EMPLOYEE, true);
        user.setId(6);

        when(userRepository.findById("6")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(6)).thenReturn(null);
        when(addressRepository.findByUserId(6)).thenReturn(Collections.emptyList());
        when(employeeRepository.findByUserId(6)).thenReturn(mock(Employee.class));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(6);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui dados de funcionário vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário for cliente e possuir dados vinculados, deve lançar IllegalStateException")
    void deveLancarExcecaoQuandoUsuarioClientePossuirDados() {
        // Arrange
        User user = new User("bianca_cli", UserRole.CLIENT, true);
        user.setId(7);

        when(userRepository.findById("7")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(7)).thenReturn(null);
        when(addressRepository.findByUserId(7)).thenReturn(Collections.emptyList());
        when(clientRepository.findByUserId(7)).thenReturn(mock(Client.class));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(7);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui dados de cliente vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário possuir atendimentos criados vinculados, deve lançar IllegalStateException")
    void deveLancarExcecaoQuandoUsuarioPossuirAtendimentosCriados() {
        // Arrange
        User user = new User("roberta_gestora", UserRole.EMPLOYEE, true);
        user.setId(8);

        when(userRepository.findById("8")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(8)).thenReturn(null);
        when(addressRepository.findByUserId(8)).thenReturn(Collections.emptyList());
        when(employeeRepository.findByUserId(8)).thenReturn(null);
        when(customerServiceRepository.findByCreatedByUserId(8))
                .thenReturn(List.of(mock(CustomerService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(8);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui atendimentos criados vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário possuir pagamentos vinculados, deve lançar IllegalStateException")
    void deveLancarExcecaoQuandoUsuarioPossuirPagamentos() {
        // Arrange
        User user = new User("carla_pg", UserRole.EMPLOYEE, true);
        user.setId(9);

        when(userRepository.findById("9")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(9)).thenReturn(null);
        when(addressRepository.findByUserId(9)).thenReturn(Collections.emptyList());
        when(employeeRepository.findByUserId(9)).thenReturn(null);
        when(customerServiceRepository.findByCreatedByUserId(9)).thenReturn(Collections.emptyList());
        when(paymentRepository.findByCreatedByUserId(9))
                .thenReturn(List.of(mock(Payment.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDeleteUseCase.execute(9);
        });

        assertEquals("Usuário não pode ser deletado pois ainda possui pagamentos vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário não possuir nenhum vínculo, deve deletar com sucesso e retornar true no output")
    void deveDeletarUsuarioComSucesso() {
        // Arrange
        User user = new User("ana_libera", UserRole.EMPLOYEE, true);
        user.setId(10);

        when(userRepository.findById("10")).thenReturn(Optional.of(user));
        when(personRepository.findByUserId(10)).thenReturn(null);
        when(addressRepository.findByUserId(10)).thenReturn(Collections.emptyList());
        when(employeeRepository.findByUserId(10)).thenReturn(null);
        when(customerServiceRepository.findByCreatedByUserId(10)).thenReturn(Collections.emptyList());
        when(paymentRepository.findByCreatedByUserId(10)).thenReturn(Collections.emptyList());

        // Act
        UserDeleteUseCaseOutputDto output = userDeleteUseCase.execute(10);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(userRepository, times(1)).delete(user);
    }
}