package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class EmployeeRoleDeleteUseCaseImpl implements EmployeeRoleDeleteUseCase {

    private final EmployeeRoleRepository employeeRoleRepository;

    public EmployeeRoleDeleteUseCaseImpl(EmployeeRoleRepository employeeRoleRepository) {
        this.employeeRoleRepository = employeeRoleRepository;
    }

    @AuditLogAnnotation
    @Override
    public EmployeeRoleDeleteUseCaseOutputDto execute(User user, Integer id) {
        var employeeRole = this.employeeRoleRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre funcionário e função inexistente"));

        this.employeeRoleRepository.delete(employeeRole);

        return new EmployeeRoleDeleteUseCaseOutputDto(true);
    }
}
