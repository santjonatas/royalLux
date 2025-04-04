package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.EmployeeRole;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeRoleGetUseCaseImpl implements EmployeeRoleGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public EmployeeRoleGetUseCaseImpl(EmployeeRoleRepository employeeRoleRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.employeeRoleRepository = employeeRoleRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EmployeeRoleGetUseCaseOutputDto> execute(User user, EmployeeRoleGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeRole> query = cb.createQuery(EmployeeRole.class);
        Root<EmployeeRole> root = query.from(EmployeeRole.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.employeeId() != null) {
            var employeeFound = employeeRepository.findById(input.employeeId().toString()).orElse(null);

            if (employeeFound != null) {
                predicates.add(cb.equal(root.get("employee"), employeeFound));
            } else {
                predicates.add(cb.isNull(root.get("employee")));
            }
        }

        if (input.roleId() != null) {
            var roleFound = roleRepository.findById(input.roleId().toString()).orElse(null);

            if (roleFound != null) {
                predicates.add(cb.equal(root.get("role"), roleFound));
            } else {
                predicates.add(cb.isNull(root.get("role")));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<EmployeeRole> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var employeesRoles = typedQuery.getResultList();

        return employeesRoles.stream()
                .map(employeeRole -> new EmployeeRoleGetUseCaseOutputDto(
                        employeeRole.getId(),
                        employeeRole.getEmployee().getId(),
                        employeeRole.getRole().getId(),
                        employeeRole.getCreatedAt(),
                        employeeRole.getUpdatedAt()
                )).toList();
    }
}
