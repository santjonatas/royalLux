package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface UserCreateUseCase {
    public UserCreateUseCaseOutputDto execute(User user, UserCreateUseCaseInputDto input);
}
