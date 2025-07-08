package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.EmployeeRole;
import jonatasSantos.royalLux.core.domain.entities.Role;
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

class RoleDeleteUseCaseImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    EmployeeRoleRepository employeeRoleRepository;

    @InjectMocks
    private RoleDeleteUseCaseImpl roleDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir função a ser deletada com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Função inexistente'")
    void deveLancarExcecaoQuandoNaoExistirFuncaoASerDeletada() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        when(roleRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleDeleteUseCase.execute(userLogged, 3);
        });

        assertEquals("Função inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando função possuir vínculos com funcionários, deve lançar IllegalStateException com mensagem específica")
    void deveLancarExcecaoQuandoFuncaoPossuirFuncionariosVinculados() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        Role role = new Role("Designer", "Responsável por design gráfico");
        role.setId(4);

        when(roleRepository.findById("4")).thenReturn(Optional.of(role));
        when(employeeRoleRepository.findByRoleId(4)).thenReturn(List.of(mock(EmployeeRole.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            roleDeleteUseCase.execute(userLogged, 4);
        });

        assertEquals("Função não pode ser deletada pois ainda possui funcionários vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando função não possuir vínculos, deve excluir com sucesso e retornar true no output")
    void deveDeletarFuncaoComSucessoQuandoNaoPossuirVinculos() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        Role role = new Role("Barbeiro", "Atuar cortando cabelo e fazendo barba");
        role.setId(3);

        when(roleRepository.findById("3")).thenReturn(Optional.of(role));
        when(employeeRoleRepository.findByRoleId(3)).thenReturn(Collections.emptyList());

        // Act
        RoleDeleteUseCaseOutputDto output = roleDeleteUseCase.execute(userLogged, 3);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(roleRepository, times(1)).delete(role);
    }
}