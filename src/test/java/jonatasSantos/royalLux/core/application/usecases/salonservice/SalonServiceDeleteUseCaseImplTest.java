package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
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

class SalonServiceDeleteUseCaseImplTest {

    @Mock
    private SalonServiceRepository salonServiceRepository;

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
    @DisplayName("Deve deletar serviço com sucesso e retornar true em propriedade success do output")
    void deveDeletarServicoComSucesso() {
        // Arrange
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));

        when(salonServiceRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(salonService));

        // Act
        SalonServiceDeleteUseCaseOutputDto output = salonServiceDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(salonServiceRepository).delete(any(SalonService.class));
        verify(salonServiceRepository, times(1)).delete(salonService);
    }
}