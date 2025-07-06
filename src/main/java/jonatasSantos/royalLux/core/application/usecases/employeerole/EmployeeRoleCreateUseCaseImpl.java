package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.EmployeeRole;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class EmployeeRoleCreateUseCaseImpl implements EmployeeRoleCreateUseCase {

    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public EmployeeRoleCreateUseCaseImpl(EmployeeRoleRepository employeeRoleRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.employeeRoleRepository = employeeRoleRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public EmployeeRoleCreateUseCaseOutputDto execute(User user, EmployeeRoleCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var employee = this.employeeRepository.findById(String.valueOf(input.employeeId()))
                .orElseThrow(() -> new EntityNotFoundException("Funcionário é inexistente"));

        var role = this.roleRepository.findById(String.valueOf(input.roleId()))
                .orElseThrow(() -> new EntityNotFoundException("Função é inexistente"));

        if(this.employeeRoleRepository.existsByEmployeeIdAndRoleId(input.employeeId(), input.roleId()))
            throw new ConflictException("Funcionário já vinculado a essa função");

        EmployeeRole employeeRole = new EmployeeRole(employee, role);
        this.employeeRoleRepository.save(employeeRole);

        return new EmployeeRoleCreateUseCaseOutputDto(employeeRole.getId());
    }
}
