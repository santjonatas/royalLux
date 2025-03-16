package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGetUseCaseImpl implements UserGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;

    public UserGetUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserGetUseCaseOutputDto> execute(UserGetUseCaseInputDto input) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.username() != null)
            predicates.add(cb.like(root.get("username"), "%" + input.username() + "%"));

        query.where(predicates.toArray(new Predicate[0]));

        var users = entityManager.createQuery(query).getResultList();

        return users.stream()
                .sorted((u1, u2) -> Long.compare(u2.getId(), u1.getId()))
                .map(user -> new UserGetUseCaseOutputDto(user.getId(), user.getUsername(), user.getRole(), user.isActive()))
                .toList();
    }
}
