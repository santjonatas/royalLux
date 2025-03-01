package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseOutputDto;

public interface UserGetUseCase {
    public UserGetUseCaseOutputDto execute(Integer Id);
}
