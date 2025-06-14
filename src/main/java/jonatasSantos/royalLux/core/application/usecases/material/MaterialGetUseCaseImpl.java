package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialGetUseCaseImpl implements MaterialGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialGetUseCaseImpl(MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<MaterialGetUseCaseOutputDto> execute(User user, MaterialGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Material> query = cb.createQuery(Material.class);
        Root<Material> root = query.from(Material.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.name() != null)
            predicates.add(cb.like(root.get("name"), "%" + input.name() + "%"));

        if (input.description() != null)
            predicates.add(cb.like(root.get("description"), "%" + input.description() + "%"));

        if (input.value() != null)
            predicates.add(cb.equal(root.get("value"), input.value()));

        if (input.quantity() != null)
            predicates.add(cb.equal(root.get("quantity"), input.quantity()));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<Material> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var materials = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)) {
            materials.forEach(materialFound -> {
                materialFound.setValue(null);
            });
        }

        return materials.stream()
                .map(materialFound -> new MaterialGetUseCaseOutputDto(
                        materialFound.getId(),
                        materialFound.getName(),
                        materialFound.getDescription(),
                        materialFound.getValue(),
                        materialFound.getQuantity(),
                        materialFound.getCreatedAt(),
                        materialFound.getUpdatedAt()
                )).toList();
    }
}
