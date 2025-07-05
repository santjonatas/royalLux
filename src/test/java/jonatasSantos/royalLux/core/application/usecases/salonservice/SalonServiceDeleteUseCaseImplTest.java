package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalonServiceDeleteUseCaseImplTest {

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @Mock
    private SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    @InjectMocks
    private SalonServiceDeleteUseCaseImpl salonServiceDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir serviço a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Serviço inexistente'")
    void deveLancarExcecaoQuandoNaoExistirServicoASerDeletado() {
        // Arrange
        when(salonServiceRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceDeleteUseCase.execute(3);
        });

        assertEquals("Serviço inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando serviço possuir materiais vinculados, deve lançar IllegalStateException com mensagem apropriada")
    void deveLancarExcecaoQuandoServicoPossuirMateriaisVinculados() {
        // Arrange
        SalonService salonService = new SalonService("Hidratação", "", LocalTime.of(0, 30), BigDecimal.valueOf(50));
        salonService.setId(1);

        when(salonServiceRepository.findById("1")).thenReturn(Optional.of(salonService));
        when(materialSalonServiceRepository.findBySalonServiceId(1))
                .thenReturn(List.of(mock(MaterialSalonService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            salonServiceDeleteUseCase.execute(1);
        });

        assertEquals("Serviço não pode ser deletado pois ainda possui materiais vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando serviço possuir atendimentos vinculados, deve lançar IllegalStateException com mensagem apropriada")
    void deveLancarExcecaoQuandoServicoPossuirAtendimentosVinculados() {
        // Arrange
        SalonService salonService = new SalonService("Coloração", "", LocalTime.of(1, 0), BigDecimal.valueOf(120));
        salonService.setId(2);

        when(salonServiceRepository.findById("2")).thenReturn(Optional.of(salonService));
        when(materialSalonServiceRepository.findBySalonServiceId(2)).thenReturn(Collections.emptyList());
        when(salonServiceCustomerServiceRepository.findBySalonServiceId(2))
                .thenReturn(List.of(mock(SalonServiceCustomerService.class)));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            salonServiceDeleteUseCase.execute(2);
        });

        assertEquals("Serviço não pode ser deletado pois ainda possui serviços de atendimentos vinculados", exception.getMessage());
    }

    @Test
    @DisplayName("Quando serviço não possuir vínculos, deve deletar com sucesso e retornar true no output")
    void deveDeletarServicoComSucessoQuandoNaoPossuirVinculos() {
        // Arrange
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.of(0, 45), BigDecimal.valueOf(40));
        salonService.setId(3);

        when(salonServiceRepository.findById("3")).thenReturn(Optional.of(salonService));
        when(materialSalonServiceRepository.findBySalonServiceId(3)).thenReturn(Collections.emptyList());
        when(salonServiceCustomerServiceRepository.findBySalonServiceId(3)).thenReturn(Collections.emptyList());

        // Act
        SalonServiceDeleteUseCaseOutputDto output = salonServiceDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertTrue(output.success());
        verify(salonServiceRepository, times(1)).delete(salonService);
    }
}