package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialSalonServiceGetUseCaseImpl implements MaterialSalonServiceGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialSalonServiceGetUseCaseImpl(MaterialSalonServiceRepository materialSalonServiceRepository, SalonServiceRepository salonServiceRepository, MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<MaterialSalonServiceGetUseCaseOutputDto> execute(User user, MaterialSalonServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MaterialSalonService> query = cb.createQuery(MaterialSalonService.class);
        Root<MaterialSalonService> root = query.from(MaterialSalonService.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.salonServiceId() != null) {
            var salonServiceFound = salonServiceRepository.findById(input.salonServiceId().toString()).orElse(null);

            if (salonServiceFound != null) {
                predicates.add(cb.equal(root.get("salonService"), salonServiceFound));
            } else {
                predicates.add(cb.isNull(root.get("salonService")));
            }
        }

        if (input.materialId() != null) {
            var materialFound = materialRepository.findById(input.materialId().toString()).orElse(null);

            if (materialFound != null) {
                predicates.add(cb.equal(root.get("material"), materialFound));
            } else {
                predicates.add(cb.isNull(root.get("material")));
            }
        }

        if (input.quantityMaterial() != null)
            predicates.add(cb.equal(root.get("quantityMaterial"), input.quantityMaterial()));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<MaterialSalonService> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var materialsSalonServices = typedQuery.getResultList();

        return materialsSalonServices.stream()
                .map(materialSalonServiceFound -> new MaterialSalonServiceGetUseCaseOutputDto(
                        materialSalonServiceFound.getId(),
                        materialSalonServiceFound.getSalonService().getId(),
                        materialSalonServiceFound.getMaterial().getId(),
                        materialSalonServiceFound.getQuantityMaterial(),
                        materialSalonServiceFound.getCreatedAt(),
                        materialSalonServiceFound.getUpdatedAt()
                )).toList();
    }
}
