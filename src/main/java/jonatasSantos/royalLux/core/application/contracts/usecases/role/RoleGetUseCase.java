package jonatasSantos.royalLux.core.application.contracts.usecases.role;

import jonatasSantos.royalLux.core.application.models.dtos.role.RoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface RoleGetUseCase {
    public List<RoleGetUseCaseOutputDto> execute(User user, RoleGetUseCaseInputDto input, Integer page, Integer size);
}
