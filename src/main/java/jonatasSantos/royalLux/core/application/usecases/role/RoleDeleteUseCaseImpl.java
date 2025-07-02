package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class RoleDeleteUseCaseImpl implements RoleDeleteUseCase {

    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;

    public RoleDeleteUseCaseImpl(RoleRepository roleRepository, EmployeeRoleRepository employeeRoleRepository) {
        this.roleRepository = roleRepository;
        this.employeeRoleRepository = employeeRoleRepository;
    }

    @Override
    public RoleDeleteUseCaseOutputDto execute(Integer id) {
        var role = this.roleRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Função inexistente"));

        var employeesRoles = this.employeeRoleRepository.findByRoleId(role.getId());

        if (!employeesRoles.isEmpty())
            throw new IllegalStateException("Cargo não pode ser deletado pois ainda possui funcionários vinculados");

        this.roleRepository.delete(role);

        return new RoleDeleteUseCaseOutputDto(true);
    }
}
