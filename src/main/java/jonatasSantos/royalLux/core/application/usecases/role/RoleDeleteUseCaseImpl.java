package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class RoleDeleteUseCaseImpl implements RoleDeleteUseCase {

    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;

    public RoleDeleteUseCaseImpl(RoleRepository roleRepository, EmployeeRoleRepository employeeRoleRepository) {
        this.roleRepository = roleRepository;
        this.employeeRoleRepository = employeeRoleRepository;
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @AuditLogAnnotation
    @Override
    public RoleDeleteUseCaseOutputDto execute(User user, Integer id) {
        var role = this.roleRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Função inexistente"));

        var employeesRoles = this.employeeRoleRepository.findByRoleId(role.getId());

        if (!employeesRoles.isEmpty())
            throw new IllegalStateException("Função não pode ser deletada pois ainda possui funcionários vinculados");

        this.roleRepository.delete(role);

        return new RoleDeleteUseCaseOutputDto(true);
    }
}
