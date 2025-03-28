package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EmployeeGetUseCaseImpl implements EmployeeGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeGetUseCaseImpl(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EmployeeGetUseCaseOutputDto> execute(User user, EmployeeGetUseCaseInputDto input, Integer page, Integer size) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

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

        if (input.title() != null)
            predicates.add(cb.like(root.get("title"), "%" + input.title() + "%"));

        if (input.salary() != null)
            predicates.add(cb.equal(root.get("salary"), input.salary()));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("id")));

        TypedQuery<Employee> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var employees = typedQuery.getResultList();

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            employees.forEach(employeeFound -> employeeFound.setSalary(null));
        }

        return employees.stream()
                .map(clientFound -> new EmployeeGetUseCaseOutputDto(
                        clientFound.getId(),
                        clientFound.getUser().getId(),
                        clientFound.getTitle(),
                        clientFound.getSalary(),
                        clientFound.getCreatedAt(),
                        clientFound.getUpdatedAt()
                )).toList();
    }
}
