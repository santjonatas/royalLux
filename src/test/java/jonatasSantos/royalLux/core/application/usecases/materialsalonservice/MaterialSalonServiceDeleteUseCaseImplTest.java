package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.*;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaterialSalonServiceDeleteUseCaseImplTest {

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MaterialSalonServiceDeleteUseCaseImpl materialSalonServiceDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceDeleteUseCase.execute(userLogged, 1);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir vínculo entre material e serviço a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Vínculo entre material e serviço é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirVinculoEntreMaterialEServicoASerDeletado() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(materialSalonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceDeleteUseCase.execute(userLogged, 1);
        });

        assertEquals("Vínculo entre material e serviço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar vínculo entre material e serviço com sucesso e retornar true em propriedade success do output")
    void deveDeletarVinculoEntreMaterialEServicoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material.setId(1);
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        MaterialSalonService materialSalonService = new MaterialSalonService(salonService, material, 2);
        materialSalonService.setId(1);

        when(materialSalonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(materialSalonService));

        // Act
        MaterialSalonServiceDeleteUseCaseOutputDto output = materialSalonServiceDeleteUseCase.execute(userLogged, 1);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(materialSalonServiceRepository).delete(any(MaterialSalonService.class));
        verify(materialSalonServiceRepository, times(1)).delete(materialSalonService);
    }
}