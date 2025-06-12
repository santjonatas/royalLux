package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
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

class EmployeeRoleCreateUseCaseImplTest {

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeRoleCreateUseCaseImpl employeeRoleCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeRoleCreateUseCaseInputDto input = new EmployeeRoleCreateUseCaseInputDto(1, 1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeRoleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir funcionário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Funcionário é inexistente'")
    void deveLancarExcecaoQuandoFuncionarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeRoleCreateUseCaseInputDto input = new EmployeeRoleCreateUseCaseInputDto(1, 1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(input.employeeId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeRoleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Funcionário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir função com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Função é inexistente'")
    void deveLancarExcecaoQuandoFuncaoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeRoleCreateUseCaseInputDto input = new EmployeeRoleCreateUseCaseInputDto(1, 1);

        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(input.employeeId())))
                .thenReturn(Optional.of(employee));

        when(roleRepository.findById(String.valueOf(input.roleId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeRoleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Função é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir vínculo entre função e funcionário, estourar exceção EntityNotFoundException com mensagem 'Funcionário já vinculado a essa função'")
    void deveLancarExcecaoQuandoJaExistirVinculoEntreFuncaoEFuncionario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeRoleCreateUseCaseInputDto input = new EmployeeRoleCreateUseCaseInputDto(1, 1);

        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(input.employeeId())))
                .thenReturn(Optional.of(employee));

        when(roleRepository.findById(String.valueOf(input.roleId())))
                .thenReturn(Optional.of(role));

        when(employeeRoleRepository.existsByEmployeeIdAndRoleId(input.employeeId(), input.roleId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            employeeRoleCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Funcionário já vinculado a essa função", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar vínculo entre funcionário e função com sucesso e retornar id de vínculo cadastrado")
    void deveCriarVinculoEntreFuncionarioEFuncaoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        EmployeeRoleCreateUseCaseInputDto input = new EmployeeRoleCreateUseCaseInputDto(1, 1);

        Employee employee = new Employee(userLogged, "Barbeiro", BigDecimal.valueOf(2000));
        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(employeeRepository.findById(String.valueOf(input.employeeId())))
                .thenReturn(Optional.of(employee));

        when(roleRepository.findById(String.valueOf(input.roleId())))
                .thenReturn(Optional.of(role));

        when(employeeRoleRepository.existsByEmployeeIdAndRoleId(input.employeeId(), input.roleId()))
                .thenReturn(false);

        when(employeeRoleRepository.save(any(EmployeeRole.class))).thenAnswer(invocation -> {
            EmployeeRole employeeRoleCreated = invocation.getArgument(0);
            employeeRoleCreated.setId(1);
            return employeeRoleCreated;
        });

        // Act
        EmployeeRoleCreateUseCaseOutputDto output = employeeRoleCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.employeeRoleId());
        verify(employeeRoleRepository).save(any(EmployeeRole.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(employeeRepository, times(1)).findById(any(String.class));
        verify(roleRepository, times(1)).findById(any(String.class));
        verify(employeeRoleRepository, times(1)).existsByEmployeeIdAndRoleId(any(Integer.class), any(Integer.class));
    }
}