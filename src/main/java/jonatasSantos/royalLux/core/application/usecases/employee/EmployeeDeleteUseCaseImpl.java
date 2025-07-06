package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDeleteUseCaseImpl implements EmployeeDeleteUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public EmployeeDeleteUseCaseImpl(EmployeeRepository employeeRepository, EmployeeRoleRepository employeeRoleRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeRoleRepository = employeeRoleRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public EmployeeDeleteUseCaseOutputDto execute(User user, Integer id) {
        var employee = this.employeeRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário inexistente"));

        var employeesRoles = this.employeeRoleRepository.findByEmployeeId(employee.getId());

        if (!employeesRoles.isEmpty())
            throw new IllegalStateException("Funcionário não pode ser deletado pois ainda possui funções vinculados");

        var salonServicesCustomerServices = this.salonServiceCustomerServiceRepository.findByEmployeeId(employee.getId());

        if (!salonServicesCustomerServices.isEmpty())
            throw new IllegalStateException("Funcionário não pode ser deletado pois ainda possui serviços de atendimentos vinculados");

        this.employeeRepository.delete(employee);

        return new EmployeeDeleteUseCaseOutputDto(true);
    }
}