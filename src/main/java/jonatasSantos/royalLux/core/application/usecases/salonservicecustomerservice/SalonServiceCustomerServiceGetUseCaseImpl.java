package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalonServiceCustomerServiceGetUseCaseImpl implements SalonServiceCustomerServiceGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public SalonServiceCustomerServiceGetUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, CustomerServiceRepository customerServiceRepository, SalonServiceRepository salonServiceRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SalonServiceCustomerServiceGetUseCaseOutputDto> execute(User user, SalonServiceCustomerServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SalonServiceCustomerService> query = cb.createQuery(SalonServiceCustomerService.class);
        Root<SalonServiceCustomerService> root = query.from(SalonServiceCustomerService.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.customerServiceId() != null) {
            var customerServiceFound = customerServiceRepository.findById(input.customerServiceId().toString()).orElse(null);

            if (customerServiceFound != null) {
                predicates.add(cb.equal(root.get("customerService"), customerServiceFound));
            } else {
                predicates.add(cb.isNull(root.get("customerService")));
            }
        }

        if (input.salonServiceId() != null) {
            var salonServiceFound = salonServiceRepository.findById(input.salonServiceId().toString()).orElse(null);

            if (salonServiceFound != null) {
                predicates.add(cb.equal(root.get("salonService"), salonServiceFound));
            } else {
                predicates.add(cb.isNull(root.get("salonService")));
            }
        }

        if (input.employeeId() != null) {
            var employeeFound = employeeRepository.findById(input.employeeId().toString()).orElse(null);

            if (employeeFound != null) {
                predicates.add(cb.equal(root.get("employee"), employeeFound));
            } else {
                predicates.add(cb.isNull(root.get("employee")));
            }
        }

        if (input.completed() != null)
            predicates.add(cb.equal(root.get("completed"), input.completed()));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<SalonServiceCustomerService> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var salonServicesCustomerService = typedQuery.getResultList();

        if (userLogged.getRole().equals(UserRole.CLIENT)) {
            salonServicesCustomerService = salonServicesCustomerService.stream()
                    .filter(salonServicesCustomerServiceFound -> {
                        var customerServiceOptional = this.customerServiceRepository
                                .findById(String.valueOf(salonServicesCustomerServiceFound.getCustomerService().getId()));

                        return customerServiceOptional
                                .map(customerService ->
                                        customerService.getClient().getUser().getId() == userLogged.getId()
                                ).orElse(false);
                    })
                    .toList();
        }

        return salonServicesCustomerService.stream()
                .map(salonServiceCustomerServiceFound -> new SalonServiceCustomerServiceGetUseCaseOutputDto(
                        salonServiceCustomerServiceFound.getId(),
                        salonServiceCustomerServiceFound.getCustomerService().getId(),
                        salonServiceCustomerServiceFound.getSalonService().getId(),
                        salonServiceCustomerServiceFound.getEmployee().getId(),
                        salonServiceCustomerServiceFound.getDate(),
                        salonServiceCustomerServiceFound.getStartTime(),
                        salonServiceCustomerServiceFound.getEstimatedFinishingTime(),
                        salonServiceCustomerServiceFound.isCompleted(),
                        salonServiceCustomerServiceFound.getCreatedAt(),
                        salonServiceCustomerServiceFound.getUpdatedAt()
                )).toList();
    }
}
