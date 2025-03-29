package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDeleteUseCaseImpl implements EmployeeDeleteUseCase {

    private final EmployeeRepository employeeRepository;

    public EmployeeDeleteUseCaseImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDeleteUseCaseOutputDto execute(Integer id) {
        var employee = this.employeeRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário inexistente"));

        this.employeeRepository.delete(employee);

        return new EmployeeDeleteUseCaseOutputDto(true);
    }
}