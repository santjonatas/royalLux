package jonatasSantos.royalLux.core.application.usecases.schedulerconfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.SchedulerConfigRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig.SchedulerConfigGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SchedulerConfig;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerConfigGetUseCaseImpl implements SchedulerConfigGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final SchedulerConfigRepository schedulerConfigRepository;
    private final UserRepository userRepository;

    public SchedulerConfigGetUseCaseImpl(SchedulerConfigRepository schedulerConfigRepository, UserRepository userRepository) {
        this.schedulerConfigRepository = schedulerConfigRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SchedulerConfigGetUseCaseOutputDto> execute(User user, SchedulerConfigGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SchedulerConfig> query = cb.createQuery(SchedulerConfig.class);
        Root<SchedulerConfig> root = query.from(SchedulerConfig.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.name() != null)
            predicates.add(cb.like(root.get("name"), "%" + input.name() + "%"));

        if (input.description() != null)
            predicates.add(cb.like(root.get("description"), "%" + input.description() + "%"));

        if (input.enabled() != null)
            predicates.add(cb.equal(root.get("enabled"), input.enabled()));

        if (input.year() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("date"), cb.literal("YYYY")),
                    input.year().toString()
            ));
        }
        if (input.month() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("date"), cb.literal("MM")),
                    String.format("%02d", input.month())
            ));
        }
        if (input.day() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("date"), cb.literal("DD")),
                    String.format("%02d", input.day())
            ));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<SchedulerConfig> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var schedulersConfig = typedQuery.getResultList();

        return schedulersConfig.stream()
                .map(schedulerConfig -> new SchedulerConfigGetUseCaseOutputDto(
                        schedulerConfig.getId(),
                        schedulerConfig.getName(),
                        schedulerConfig.getDescription(),
                        schedulerConfig.getDate(),
                        schedulerConfig.getEnabled(),
                        schedulerConfig.getCreatedAt(),
                        schedulerConfig.getUpdatedAt()
                )).toList();
    }
}
