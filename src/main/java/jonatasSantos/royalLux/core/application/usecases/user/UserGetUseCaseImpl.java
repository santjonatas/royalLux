package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserGetUseCaseImpl implements UserGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;

    public UserGetUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserGetUseCaseOutputDto> execute(User user, UserGetUseCaseInputDto input, Integer page, Integer size) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.username() != null)
            predicates.add(cb.like(root.get("username"), "%" + input.username() + "%"));

        if (input.role() != null)
            predicates.add(cb.like(root.get("role"), "%" + input.role() + "%"));

        if (input.active() != null)
            predicates.add(cb.equal(root.get("active"), input.active()));

        query.where(predicates.toArray(new Predicate[0]));

        query.orderBy(cb.desc(root.get("id")));

        TypedQuery<User> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        int setsize = (size != null && size > 0) ? size : 10;

        typedQuery.setFirstResult(setPage * setsize);
        typedQuery.setMaxResults(setsize);

        var users = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            users = Stream.concat(
                    users.stream().filter(userFound -> !userFound.getRole().equals(UserRole.CLIENT)),
                    users.stream().filter(userFound -> userFound.getId() == userLogged.getId())
            ).collect(Collectors.toList());
        }

        return users.stream()
                .map(userFound -> new UserGetUseCaseOutputDto(
                        userFound.getId(),
                        userFound.getUsername(),
                        userFound.getRole(),
                        userFound.isActive(),
                        userFound.getCreatedAt(),
                        userFound.getUpdatedAt())).toList();
    }
}