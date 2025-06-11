package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeCreateUseCaseImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeCreateUseCaseImpl employeeCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeCreateUseCaseInputDto input = new EmployeeCreateUseCaseInputDto(3, "Barbeiro", BigDecimal.valueOf(2000));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir usuário de funcionário a ser criado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioDeFuncionarioASerCriadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeCreateUseCaseInputDto input = new EmployeeCreateUseCaseInputDto(3, "Barbeiro", BigDecimal.valueOf(2000));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando role de usuário de funcionário a ser criado não for EMPLOYEE, estourar exceção IllegalArgumentException com mensagem 'Usuário deve ser um funcionário'")
    void deveLancarExcecaoQuandoRoleDeUsuarioDeClienteASerCriadoNaoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeCreateUseCaseInputDto input = new EmployeeCreateUseCaseInputDto(3, "Barbeiro", BigDecimal.valueOf(2000));

        User employee = new User("mateus_2", UserRole.CLIENT, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(employee));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário deve ser um funcionário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário já estiver vinculado a um funcionário, estourar exceção ConflictException com mensagem 'Funcionário já vinculado a um usuário'")
    void deveLancarExcecaoQuandoUsuarioJaEstiverVinculadoAUmFuncionario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeCreateUseCaseInputDto input = new EmployeeCreateUseCaseInputDto(3, "Barbeiro", BigDecimal.valueOf(2000));

        User employee = new User("mateus_2", UserRole.EMPLOYEE, true);
        employee.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByUserId(employee.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            employeeCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Funcionário já vinculado a um usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar funcionário com sucesso e retornar id de funcionário cadastrado")
    void deveCriarFuncionarioComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeCreateUseCaseInputDto input = new EmployeeCreateUseCaseInputDto(3, "Barbeiro", BigDecimal.valueOf(2000));

        User employee = new User("mateus_2", UserRole.EMPLOYEE, true);
        employee.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByUserId(employee.getId()))
                .thenReturn(false);

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee employeeCreated = invocation.getArgument(0);
            employeeCreated.setId(3);
            return employeeCreated;
        });

        // Act
        EmployeeCreateUseCaseOutputDto output = employeeCreateUseCase.execute(userLogged, input);

        //  Assert
        assertNotNull(output);
        assertEquals(3, output.employeeId());
        verify(employeeRepository).save(any(Employee.class));
        verify(userRepository, times(2)).findById(any(String.class));
        verify(employeeRepository, times(1)).existsByUserId(any(Integer.class));
    }
}