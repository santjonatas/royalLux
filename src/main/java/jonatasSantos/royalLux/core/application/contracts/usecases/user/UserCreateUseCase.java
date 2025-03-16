package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseOutputDto;

public interface UserCreateUseCase {
    public UserCreateUseCaseOutputDto execute(UserCreateUseCaseInputDto input);
}
