package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.User;
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

class MaterialSalonServiceUpdateUseCaseImplTest {

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @InjectMocks
    private MaterialSalonServiceUpdateUseCaseImpl materialSalonServiceUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir vínculo entre material e serviço a ser atualizado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Vínculo entre material e serviço é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirVinculoEntreMaterialEServico() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        MaterialSalonServiceUpdateUseCaseInputDto input = new MaterialSalonServiceUpdateUseCaseInputDto(1);

        when(materialSalonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceUpdateUseCase.execute(userLogged, 3, input);
        });

        assertEquals("Vínculo entre material e serviço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar vínculo entre material e serviço com sucesso e retornar true em propriedade success do output")
    void deveAtualizarVinculoEntreMaterialEServicoComSucesso() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        MaterialSalonServiceUpdateUseCaseInputDto input = new MaterialSalonServiceUpdateUseCaseInputDto(1);

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material.setId(1);
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        MaterialSalonService materialSalonService = new MaterialSalonService(salonService, material, 2);
        materialSalonService.setId(1);

        when(materialSalonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(materialSalonService));

        // Act
        MaterialSalonServiceUpdateUseCaseOutputDto output = materialSalonServiceUpdateUseCase.execute(userLogged, 1, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(materialSalonServiceRepository).save(any(MaterialSalonService.class));
        verify(materialSalonServiceRepository, times(1)).findById(String.valueOf(1));
    }
}