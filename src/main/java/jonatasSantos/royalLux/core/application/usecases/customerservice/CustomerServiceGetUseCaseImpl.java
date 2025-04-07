package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceGetUseCaseImpl implements CustomerServiceGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final CustomerServiceRepository customerServiceRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public CustomerServiceGetUseCaseImpl(CustomerServiceRepository customerServiceRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.customerServiceRepository = customerServiceRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CustomerServiceGetUseCaseOutputDto> execute(User user, CustomerServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerService> query = cb.createQuery(CustomerService.class);
        Root<CustomerService> root = query.from(CustomerService.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.createdByUserId() != null) {
            var userFound = userRepository.findById(input.createdByUserId().toString()).orElse(null);

            if (userFound != null) {
                predicates.add(cb.equal(root.get("createdByUser"), userFound));
            } else {
                predicates.add(cb.isNull(root.get("createdByUser")));
            }
        }

        if (input.clientId() != null) {
            var clientFound = clientRepository.findById(input.clientId().toString()).orElse(null);

            if (clientFound != null) {
                predicates.add(cb.equal(root.get("client"), clientFound));
            } else {
                predicates.add(cb.isNull(root.get("client")));
            }
        }

        if (input.status() != null)
            predicates.add(cb.like(root.get("status"), "%" + input.status() + "%"));

        // startTime

        if (input.startTimeYear() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("startTime"), cb.literal("YYYY")),
                    input.startTimeYear().toString()
            ));
        }
        if (input.startTimeMonth() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("startTime"), cb.literal("MM")),
                    String.format("%02d", input.startTimeMonth())
            ));
        }
        if (input.startTimeDay() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("startTime"), cb.literal("DD")),
                    String.format("%02d", input.startTimeDay())
            ));
        }
        if (input.startTimeHour() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("startTime"), cb.literal("HH24")),
                    String.format("%02d", input.startTimeHour())
            ));
        }
        if (input.startTimeMinute() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("startTime"), cb.literal("MI")),
                    String.format("%02d", input.startTimeMinute())
            ));
        }

        // estimatedFinishingTime

        if (input.estimatedFinishingTimeYear() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("estimatedFinishingTime"), cb.literal("YYYY")),
                    input.estimatedFinishingTimeYear().toString()
            ));
        }
        if (input.startTimeMonth() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("estimatedFinishingTime"), cb.literal("MM")),
                    String.format("%02d", input.estimatedFinishingTimeMonth())
            ));
        }
        if (input.estimatedFinishingTimeDay() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("estimatedFinishingTime"), cb.literal("DD")),
                    String.format("%02d", input.estimatedFinishingTimeDay())
            ));
        }
        if (input.estimatedFinishingTimeHour() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("estimatedFinishingTime"), cb.literal("HH24")),
                    String.format("%02d", input.estimatedFinishingTimeHour())
            ));
        }
        if (input.estimatedFinishingTimeMinute() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("estimatedFinishingTime"), cb.literal("MI")),
                    String.format("%02d", input.estimatedFinishingTimeMinute())
            ));
        }

        // finishingTime

        if (input.finishingTimeYear() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("finishingTime"), cb.literal("YYYY")),
                    input.finishingTimeYear().toString()
            ));
        }
        if (input.startTimeMonth() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("finishingTime"), cb.literal("MM")),
                    String.format("%02d", input.finishingTimeMonth())
            ));
        }
        if (input.finishingTimeDay() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("finishingTime"), cb.literal("DD")),
                    String.format("%02d", input.finishingTimeDay())
            ));
        }
        if (input.finishingTimeHour() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("finishingTime"), cb.literal("HH24")),
                    String.format("%02d", input.finishingTimeHour())
            ));
        }
        if (input.finishingTimeMinute() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("finishingTime"), cb.literal("MI")),
                    String.format("%02d", input.finishingTimeMinute())
            ));
        }

        if (input.totalValue() != null)
            predicates.add(cb.equal(root.get("totalValue"), input.totalValue()));

        if (input.details() != null)
            predicates.add(cb.like(root.get("details"), "%" + input.details() + "%"));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<CustomerService> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var customerService = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            customerService = customerService.stream().filter(customerServiceFound -> customerServiceFound.getClient().getUser().getId() == userLogged.getId()).toList();
        }

        return customerService.stream()
                .map(customerServiceFound -> new CustomerServiceGetUseCaseOutputDto(
                        customerServiceFound.getId(),
                        customerServiceFound.getCreatedByUser().getId(),
                        customerServiceFound.getClient().getId(),
                        customerServiceFound.getStatus(),
                        customerServiceFound.getStartTime(),
                        customerServiceFound.getEstimatedFinishingTime(),
                        customerServiceFound.getFinishingTime(),
                        customerServiceFound.getTotalValue(),
                        customerServiceFound.getDetails(),
                        customerServiceFound.getCreatedAt(),
                        customerServiceFound.getUpdatedAt()
                )).toList();
    }
}
