package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface RoleDeleteUseCase {
    public RoleDeleteUseCaseOutputDto execute(User user, Integer id);
}
