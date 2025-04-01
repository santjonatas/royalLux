package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;

public interface RoleDeleteUseCase {
    public RoleDeleteUseCaseOutputDto execute(Integer id);
}
