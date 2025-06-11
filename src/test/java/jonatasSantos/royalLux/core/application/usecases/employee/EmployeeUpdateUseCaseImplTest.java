package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseOutputDto;
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
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeUpdateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeUpdateUseCaseImpl employeeUpdateUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeUpdateUseCaseInputDto input = new EmployeeUpdateUseCaseInputDto("Barbeiro", BigDecimal.valueOf(2000));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir funcionário a ser atualizado, estourar exceção EntityNotFoundException com mensagem 'Funcionário é inexistente'")
    void deveLancarExcecaoQuandoFuncionarioASerAtualizadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeUpdateUseCaseInputDto input = new EmployeeUpdateUseCaseInputDto("Barbeiro", BigDecimal.valueOf(2000));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Funcionário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for funcionário e tentar atualizar outro funcionário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar outro funcionário'")
    void deveLancarExcecaoQuandoUsuarioLogadoForFuncionarioETentarAtualizarOutroFuncionario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);
        userLogged.setId(2);
        EmployeeUpdateUseCaseInputDto input = new EmployeeUpdateUseCaseInputDto("Barbeiro", BigDecimal.valueOf(2000));
        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        employee.setId(2);

        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        user.setId(3);
        Employee employee2 = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        user.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(employee2));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            employeeUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar outro funcionário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar funcionário com sucesso e retornar true em propriedade success do output")
    void deveAtualizarFuncionarioComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(2);
        EmployeeUpdateUseCaseInputDto input = new EmployeeUpdateUseCaseInputDto("Barbeiro", BigDecimal.valueOf(2000));
        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        employee.setId(2);

        User user = new User("mateus_2", UserRole.EMPLOYEE, true);
        user.setId(3);
        Employee employee2 = new Employee(user, "Cabelereiro", BigDecimal.valueOf(2000));
        employee2.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(employee2));

        // Act
        EmployeeUpdateUseCaseOutputDto output = employeeUpdateUseCase.execute(
                    userLogged,
                    3,
                    input
            );

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(employeeRepository).save(any(Employee.class));
        verify(userRepository, times(1)).findById(String.valueOf(2));
        verify(employeeRepository, times(1)).findById(String.valueOf(3));
    }
}