package jonatasSantos.royalLux.core.application.contracts.usecases.employeerole;


import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface EmployeeRoleDeleteUseCase {
    public EmployeeRoleDeleteUseCaseOutputDto execute(User user, Integer id);
}
