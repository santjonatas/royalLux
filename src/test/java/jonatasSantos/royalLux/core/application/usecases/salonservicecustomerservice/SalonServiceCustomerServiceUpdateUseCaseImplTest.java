package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCaseInputDto;
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
import static org.mockito.Mockito.when;

class SalonServiceCustomerServiceUpdateUseCaseImplTest {

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SalonServiceCustomerServiceUpdateUseCaseImpl salonServiceCustomerServiceUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceUpdateUseCaseInputDto input = new SalonServiceCustomerServiceUpdateUseCaseInputDto(1, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não vínculo entre atendimento e serviço, estourar exceção EntityNotFoundException com mensagem 'Vínculo entre serviço e atendimento é inexistente'")
    void deveLancarExcecaoQuandoVinculoEntreAtendimentoEServicoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCustomerServiceUpdateUseCaseInputDto input = new SalonServiceCustomerServiceUpdateUseCaseInputDto(1, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(salonServiceCustomerServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCustomerServiceUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Vínculo entre serviço e atendimento é inexistente", exception.getMessage());
    }


}