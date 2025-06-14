package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseOutputDto;
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

class MaterialSalonServiceCreateUseCaseImplTest {

    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MaterialSalonServiceCreateUseCaseImpl materialSalonServiceCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialSalonServiceCreateUseCaseInputDto input = new MaterialSalonServiceCreateUseCaseInputDto(1, 1, 2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir material com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Material é inexistente'")
    void deveLancarExcecaoQuandoMaterialNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialSalonServiceCreateUseCaseInputDto input = new MaterialSalonServiceCreateUseCaseInputDto(1, 1, 2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(materialRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Material é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir serviço com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Serviço é inexistente'")
    void deveLancarExcecaoQuandoServicoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialSalonServiceCreateUseCaseInputDto input = new MaterialSalonServiceCreateUseCaseInputDto(1, 1, 2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);

        when(materialRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.of(material));

        when(salonServiceRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Serviço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir material vinculado ao serviço, estourar exceção ConflictException com mensagem 'Material já vinculado a esse serviço'")
    void deveLancarExcecaoQuandoVinculoEntreMaterialEServicoJaExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialSalonServiceCreateUseCaseInputDto input = new MaterialSalonServiceCreateUseCaseInputDto(1, 1, 2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material.setId(1);

        when(materialRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.of(material));

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        when(salonServiceRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.of(salonService));

        when(materialSalonServiceRepository.existsByMaterialIdAndSalonServiceId(input.materialId(), salonService.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            materialSalonServiceCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Material já vinculado a esse serviço", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar vínculo entre material e serviço com sucesso e retornar id de vínculo cadastrado")
    void deveCriarVinculoEntreMaterialEServicoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        MaterialSalonServiceCreateUseCaseInputDto input = new MaterialSalonServiceCreateUseCaseInputDto(1, 1, 2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material.setId(1);

        when(materialRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.of(material));

        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        when(salonServiceRepository.findById(String.valueOf(input.materialId())))
                .thenReturn(Optional.of(salonService));

        when(materialSalonServiceRepository.existsByMaterialIdAndSalonServiceId(input.materialId(), salonService.getId()))
                .thenReturn(false);

        when(materialSalonServiceRepository.save(any(MaterialSalonService.class))).thenAnswer(invocation -> {
            MaterialSalonService materialSalonServiceCreated = invocation.getArgument(0);
            materialSalonServiceCreated.setId(1);
            return materialSalonServiceCreated;
        });

        // Act
        MaterialSalonServiceCreateUseCaseOutputDto output = materialSalonServiceCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.materialServiceId());
        verify(materialSalonServiceRepository).save(any(MaterialSalonService.class));
        verify(userRepository, times(1)).findById(any(String.class));
        verify(materialRepository, times(1)).findById(any(String.class));
        verify(salonServiceRepository, times(1)).findById(any(String.class));
        verify(materialSalonServiceRepository, times(1)).existsByMaterialIdAndSalonServiceId(any(Integer.class), any(Integer.class));
    }
}