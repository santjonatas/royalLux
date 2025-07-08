package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseOutputDto;
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

class SalonServiceUpdateUseCaseImplTest {

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @InjectMocks
    private SalonServiceUpdateUseCaseImpl salonServiceUpdateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir material a ser atualizado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Serviço é inexistente'")
    void deveLancarExcecaoQuandoNaoExistirServicoASerAtualizado() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        SalonServiceUpdateUseCaseInputDto input = new SalonServiceUpdateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));

        when(salonServiceRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceUpdateUseCase.execute(userLogged, 3, input);
        });

        assertEquals("Serviço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir serviço com o mesmo nome, estourar exceção ConflictException com mensagem 'Serviço já existente com este nome'")
    void deveLancarExcecaoQuandoJaExistirServicoComOMesmoNome() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        SalonServiceUpdateUseCaseInputDto input = new SalonServiceUpdateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        when(salonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(salonService));

        when(salonServiceRepository.existsByNameAndIdNot(input.name(), 1))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            salonServiceUpdateUseCase.execute(userLogged, 1, input);
        });

        assertEquals("Serviço já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar serviço com sucesso e retornar true em propriedade success do output")
    void deveAtualizarServicoComSucesso() {
        // Arrange
        User userLogged = new User("maicon", UserRole.ADMIN, true);

        SalonServiceUpdateUseCaseInputDto input = new SalonServiceUpdateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        when(salonServiceRepository.findById(String.valueOf(1)))
                .thenReturn(Optional.of(salonService));

        when(salonServiceRepository.existsByNameAndIdNot(input.name(), 1))
                .thenReturn(false);

        // Act
        SalonServiceUpdateUseCaseOutputDto output = salonServiceUpdateUseCase.execute(userLogged, 1, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(salonServiceRepository).save(any(SalonService.class));
        verify(salonServiceRepository, times(1)).findById(String.valueOf(1));
        verify(salonServiceRepository, times(1)).existsByNameAndIdNot(input.name(), 1);
    }
}