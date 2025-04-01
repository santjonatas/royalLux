package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface RoleCreateUseCase {
    public RoleCreateUseCaseOutputDto execute(User user, RoleCreateUseCaseInputDto input);
}
