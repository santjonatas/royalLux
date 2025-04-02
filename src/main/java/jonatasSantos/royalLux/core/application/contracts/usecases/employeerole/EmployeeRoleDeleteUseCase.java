package jonatasSantos.royalLux.core.application.contracts.usecases.employeerole;


import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleDeleteUseCaseOutputDto;

public interface EmployeeRoleDeleteUseCase {
    public EmployeeRoleDeleteUseCaseOutputDto execute(Integer id);
}
