package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ClientGetUseCaseImpl implements ClientGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientGetUseCaseImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ClientGetUseCaseOutputDto> execute(User user, ClientGetUseCaseInputDto input, Integer page, Integer size) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

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

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("id")));

        TypedQuery<Client> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var clients = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            clients = Stream.concat(
                    clients.stream().filter(clientFound -> !clientFound.getUser().getRole().equals(UserRole.CLIENT)),
                    clients.stream().filter(clientFound -> clientFound.getUser().getId() == userLogged.getId())
            ).toList();
        }

        return clients.stream()
                .map(clientFound -> new ClientGetUseCaseOutputDto(
                        clientFound.getId(),
                        clientFound.getUser().getId(),
                        clientFound.getCreatedAt(),
                        clientFound.getUpdatedAt()
                )).toList();
    }
}