package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetMeUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface UserGetMeUseCase {
    public UserGetMeUseCaseOutputDto execute(User user);
}
