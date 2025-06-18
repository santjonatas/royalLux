package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseInputDto;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MaterialSalonServiceGetUseCaseImplTest {
    @Mock
    private MaterialSalonServiceRepository materialSalonServiceRepository;

    @Mock
    private SalonServiceRepository salonServiceRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<MaterialSalonService> criteriaQuery;

    @Mock
    private Root<MaterialSalonService> root;

    @Mock
    private TypedQuery<MaterialSalonService> typedQuery;

    @InjectMocks
    private MaterialSalonServiceGetUseCaseImpl materialSalonServiceGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = MaterialSalonServiceGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(materialSalonServiceGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        MaterialSalonServiceGetUseCaseInputDto input = new MaterialSalonServiceGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialSalonServiceGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar todos os vínculos entre material e serviço com sucesso")
    void deveRetornarTodosVinculosEntreMaterialEServico() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        MaterialSalonServiceGetUseCaseInputDto input = new MaterialSalonServiceGetUseCaseInputDto(null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material.setId(1);
        SalonService salonService = new SalonService("Corte de cabelo", "", LocalTime.parse("00:45:00"), BigDecimal.valueOf(40));
        salonService.setId(1);

        MaterialSalonService materialSalonService1 = new MaterialSalonService(salonService, material, 2);
        materialSalonService1.setId(1);

        var materialsSalonServicesFromDb = List.of(materialSalonService1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(MaterialSalonService.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(MaterialSalonService.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(materialsSalonServicesFromDb);

        // Act
        var result = materialSalonServiceGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(materialSalonService -> materialSalonService.id().equals(1))); // MaterialSalonService
    }
}