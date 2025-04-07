package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalonServiceGetUseCaseImpl implements SalonServiceGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final SalonServiceRepository salonServiceRepository;
    private final UserRepository userRepository;

    public SalonServiceGetUseCaseImpl(SalonServiceRepository salonServiceRepository, UserRepository userRepository) {
        this.salonServiceRepository = salonServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SalonServiceGetUseCaseOutputDto> execute(User user, SalonServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SalonService> query = cb.createQuery(SalonService.class);
        Root<SalonService> root = query.from(SalonService.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.name() != null)
            predicates.add(cb.like(root.get("name"), "%" + input.name() + "%"));

        if (input.description() != null)
            predicates.add(cb.like(root.get("description"), "%" + input.description() + "%"));

        if (input.estimatedTime() != null)
            predicates.add(cb.equal(root.get("estimatedTime"), input.estimatedTime()));

        if (input.value() != null)
            predicates.add(cb.equal(root.get("value"), input.value()));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<SalonService> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var salonService = typedQuery.getResultList();

        return salonService.stream()
                .map(salonServiceFound -> new SalonServiceGetUseCaseOutputDto(
                        salonServiceFound.getId(),
                        salonServiceFound.getName(),
                        salonServiceFound.getDescription(),
                        salonServiceFound.getEstimatedTime(),
                        salonServiceFound.getValue(),
                        salonServiceFound.getCreatedAt(),
                        salonServiceFound.getUpdatedAt()
                )).toList();
    }
}
