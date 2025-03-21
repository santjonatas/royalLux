package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Person;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonGetUseCaseImpl implements PersonGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonGetUseCaseImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PersonGetUseCaseOutputDto> execute(User user, PersonGetUseCaseInputDto input){
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = cb.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.userId() != null) {
            var userFound = userRepository.findById(input.userId().toString()).orElse(null);

            if (userFound != null) {
                predicates.add(cb.equal(root.get("user"), userFound));
            } else {
                predicates.add(cb.isNull(root.get("user")));
            }
        }

        if (input.name() != null)
            predicates.add(cb.like(root.get("name"), "%" + input.name() + "%"));

        if (input.dateBirth() != null)
            predicates.add(cb.like(root.get("dateBirth"), "%" + input.dateBirth() + "%"));

        if (input.cpf() != null)
            predicates.add(cb.like(root.get("cpf"), "%" + input.cpf() + "%"));

        if (input.phone() != null)
            predicates.add(cb.like(root.get("phone"), "%" + input.phone() + "%"));

        if (input.phone() != null)
            predicates.add(cb.like(root.get("phone"), "%" + input.phone() + "%"));

        query.where(predicates.toArray(new Predicate[0]));

        var persons = entityManager.createQuery(query).getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            persons = Stream.concat(
                    persons.stream().filter(personFound -> !personFound.getUser().getRole().equals(UserRole.CLIENT)),
                    persons.stream().filter(personFound -> personFound.getUser().getId() == userLogged.getId())
            ).collect(Collectors.toList());
        }

        return persons.stream()
                .sorted((u1, u2) -> Long.compare(u2.getId(), u1.getId()))
                .map(personFound -> new PersonGetUseCaseOutputDto(
                        personFound.getId(),
                        personFound.getUser().getId(),
                        personFound.getName(),
                        personFound.getDateBirth(),
                        personFound.getCpf(),
                        personFound.getPhone(),
                        personFound.getEmail(),
                        personFound.getCreatedAt(),
                        personFound.getUpdatedAt()
                )).toList();
    }
}