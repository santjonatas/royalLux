package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Address;
import jonatasSantos.royalLux.core.domain.entities.Person;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AddressGetUseCaseImpl implements AddressGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressGetUseCaseImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressGetUseCaseOutputDto> execute(User user, AddressGetUseCaseInputDto input, Integer page, Integer size) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Address> query = cb.createQuery(Address.class);
        Root<Address> root = query.from(Address.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.userId() != null) {
            var userFound = this.userRepository.findById(input.userId().toString()).orElse(null);

            if (userFound != null) {
                predicates.add(cb.equal(root.get("user"), userFound));
            } else {
                predicates.add(cb.isNull(root.get("user")));
            }
        }

        if (input.street() != null)
            predicates.add(cb.like(root.get("street"), "%" + input.street() + "%"));

        if (input.houseNumber() != null)
            predicates.add(cb.like(root.get("houseNumber"), "%" + input.houseNumber() + "%"));

        if (input.complement() != null)
            predicates.add(cb.like(root.get("complement"), "%" + input.complement() + "%"));

        if (input.neighborhood() != null)
            predicates.add(cb.like(root.get("neighborhood"), "%" + input.neighborhood() + "%"));

        if (input.city() != null)
            predicates.add(cb.like(root.get("city"), "%" + input.city() + "%"));

        if (input.state() != null)
            predicates.add(cb.like(root.get("state"), "%" + input.state() + "%"));

        if (input.cep() != null)
            predicates.add(cb.like(root.get("cep"), "%" + input.cep() + "%"));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("id")));

        TypedQuery<Address> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var addresses = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            addresses = Stream.concat(
                    addresses.stream().filter(addressFound -> addressFound.getUser().getRole().equals(UserRole.CLIENT)),
                    addresses.stream().filter(addressFound -> addressFound.getUser().getId() == userLogged.getId())
            ).toList();
        }

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            addresses = addresses.stream().filter(addressFound -> addressFound.getUser().getId() == userLogged.getId()).toList();
        }

        return addresses.stream()
                .map(addressFound -> new AddressGetUseCaseOutputDto(
                        addressFound.getId(),
                        addressFound.getUser().getId(),
                        addressFound.getStreet(),
                        addressFound.getHouseNumber(),
                        addressFound.getComplement(),
                        addressFound.getNeighborhood(),
                        addressFound.getCity(),
                        addressFound.getState(),
                        addressFound.getCep(),
                        addressFound.getCreatedAt(),
                        addressFound.getUpdatedAt()
                )).toList();
    }
}
