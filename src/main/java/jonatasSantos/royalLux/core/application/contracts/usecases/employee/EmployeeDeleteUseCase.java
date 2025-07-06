package jonatasSantos.royalLux.core.application.contracts.usecases.employee;

import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface EmployeeDeleteUseCase {
    public EmployeeDeleteUseCaseOutputDto execute(User user, Integer id);
}
