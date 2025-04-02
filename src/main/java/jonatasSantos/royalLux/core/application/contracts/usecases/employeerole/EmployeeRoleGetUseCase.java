package jonatasSantos.royalLux.core.application.contracts.usecases.employeerole;

import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface EmployeeRoleGetUseCase {
    public List<EmployeeRoleGetUseCaseOutputDto> execute(User user, EmployeeRoleGetUseCaseInputDto input, Integer page, Integer size);
}
