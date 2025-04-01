package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface RoleUpdateUseCase {
    public RoleUpdateUseCaseOutputDto execute(User user, Integer roleId, RoleUpdateUseCaseInputDto input);
}
