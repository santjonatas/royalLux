package jonatasSantos.royalLux.core.application.contracts.usecases.employee;

import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface EmployeeCreateUseCase {
    public EmployeeCreateUseCaseOutputDto execute(User user, EmployeeCreateUseCaseInputDto input);
}