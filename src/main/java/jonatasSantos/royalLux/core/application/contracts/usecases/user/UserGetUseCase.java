package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface UserGetUseCase {
    public List<UserGetUseCaseOutputDto> execute(User user, UserGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
