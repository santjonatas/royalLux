package jonatasSantos.royalLux.core.application.contracts.usecases.employee;

import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface EmployeeGetUseCase {
    public List<EmployeeGetUseCaseOutputDto> execute(User user, EmployeeGetUseCaseInputDto input, Integer page, Integer size);
}