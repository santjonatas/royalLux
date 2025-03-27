package jonatasSantos.royalLux.core.application.usecases.employee;

import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public class EmployeeGetUseCaseImpl implements EmployeeGetUseCase {
    @Override
    public List<EmployeeGetUseCaseOutputDto> execute(User user, EmployeeGetUseCaseInputDto input, Integer page, Integer size) {
        return List.of();
    }
}
