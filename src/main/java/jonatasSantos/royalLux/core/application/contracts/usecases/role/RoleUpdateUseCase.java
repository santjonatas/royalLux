package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseOutputDto;

public interface RoleUpdateUseCase {
    public RoleUpdateUseCaseOutputDto execute(Integer roleId, RoleUpdateUseCaseInputDto input);
}
