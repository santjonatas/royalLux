package jonatasSantos.royalLux.core.application.contracts.usecases.employeerole;

import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface EmployeeRoleCreateUseCase {
    public EmployeeRoleCreateUseCaseOutputDto execute(User user, EmployeeRoleCreateUseCaseInputDto input);
}
