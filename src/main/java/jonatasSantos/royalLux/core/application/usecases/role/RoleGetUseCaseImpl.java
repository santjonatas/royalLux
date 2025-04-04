package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Role;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleGetUseCaseImpl implements RoleGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleGetUseCaseImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RoleGetUseCaseOutputDto> execute(User user, RoleGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.name() != null)
            predicates.add(cb.like(root.get("name"), "%" + input.name() + "%"));

        if (input.detail() != null)
            predicates.add(cb.like(root.get("detail"), "%" + input.detail() + "%"));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<Role> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var roles = typedQuery.getResultList();

        return roles.stream()
                .map(personFound -> new RoleGetUseCaseOutputDto(
                        personFound.getId(),
                        personFound.getName(),
                        personFound.getDetail(),
                        personFound.getCreatedAt(),
                        personFound.getUpdatedAt()
                )).toList();
    }
}
