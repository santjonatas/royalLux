package jonatasSantos.royalLux.core.application.contracts.usecases.employee;

import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;

public interface EmployeeDeleteUseCase {
    public EmployeeDeleteUseCaseOutputDto execute(Integer id);
}
