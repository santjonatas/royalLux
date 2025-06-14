package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
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

class SalonServiceCreateUseCaseImplTest {

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SalonServiceCreateUseCaseImpl salonServiceCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCreateUseCaseInputDto input = new SalonServiceCreateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"),BigDecimal.valueOf(40));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir serviço com mesmo nome, estourar exceção ConflictException com mensagem 'Serviço já existente com este nome'")
    void deveLancarExcecaoQuandoServicoJaExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCreateUseCaseInputDto input = new SalonServiceCreateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"),BigDecimal.valueOf(40));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(salonServiceRepository.existsByName(input.name()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            salonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Serviço já existente com este nome", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar serviço com sucesso e retornar id de serviço cadastrado")
    void deveCriarServicoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        SalonServiceCreateUseCaseInputDto input = new SalonServiceCreateUseCaseInputDto("Corte de cabelo", "", LocalTime.parse("00:45:00"),BigDecimal.valueOf(40));

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(salonServiceRepository.existsByName(input.name()))
                .thenReturn(false);

        when(salonServiceRepository.save(any(SalonService.class))).thenAnswer(invocation -> {
            SalonService salonServiceCreated = invocation.getArgument(0);
            salonServiceCreated.setId(1);
            return salonServiceCreated;
        });

        // Act
        SalonServiceCreateUseCaseOutputDto output = salonServiceCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.salonServiceId());
        verify(salonServiceRepository).save(any(SalonService.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(salonServiceRepository, times(1)).existsByName(any(String.class));
    }
}