package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface UserUpdateUseCase {
    public UserUpdateUseCaseOutputDto execute (User user, Integer id, UserUpdateUseCaseInputDto input);
}
