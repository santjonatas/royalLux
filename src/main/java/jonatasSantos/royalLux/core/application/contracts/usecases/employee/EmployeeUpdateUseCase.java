package jonatasSantos.royalLux.core.application.contracts.usecases.employee;

import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface EmployeeUpdateUseCase {
    public EmployeeUpdateUseCaseOutputDto execute(User user, Integer employeeId, EmployeeUpdateUseCaseInputDto input);
}