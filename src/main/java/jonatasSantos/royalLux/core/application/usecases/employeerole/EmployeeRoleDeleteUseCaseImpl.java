package jonatasSantos.royalLux.core.application.usecases.employeerole;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class EmployeeRoleDeleteUseCaseImpl implements EmployeeRoleDeleteUseCase {

    private final EmployeeRoleRepository employeeRoleRepository;

    public EmployeeRoleDeleteUseCaseImpl(EmployeeRoleRepository employeeRoleRepository) {
        this.employeeRoleRepository = employeeRoleRepository;
    }

    @Override
    public EmployeeRoleDeleteUseCaseOutputDto execute(Integer id) {
        var employeeRole = this.employeeRoleRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre funcionário e função inexistente"));

        this.employeeRoleRepository.delete(employeeRole);

        return new EmployeeRoleDeleteUseCaseOutputDto(true);
    }
}
