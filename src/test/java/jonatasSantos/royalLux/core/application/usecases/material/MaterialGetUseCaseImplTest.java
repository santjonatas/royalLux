package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MaterialGetUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Material> criteriaQuery;

    @Mock
    private Root<Material> root;

    @Mock
    private TypedQuery<Material> typedQuery;

    @InjectMocks
    private MaterialGetUseCaseImpl materialGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = MaterialGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(materialGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        MaterialGetUseCaseInputDto input = new MaterialGetUseCaseInputDto(null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            materialGetUseCase.execute(
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
    @DisplayName("Quando usuário for CLIENT, deve ocultar valor")
    void deveOcultarValorQuandoUsuarioForCliente() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        MaterialGetUseCaseInputDto input = new MaterialGetUseCaseInputDto(null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        Material material1 = new Material("Tinta de cabelo", "Tinta vermelha", BigDecimal.valueOf(40), 5);
        material1.setId(1);

        var materialsFromDb = List.of(material1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Material.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Material.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(materialsFromDb);

        // Act
        var result = materialGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(material -> material.id().equals(1)));
        assertTrue(result.stream().anyMatch(material -> material.value() == null));
    }
}