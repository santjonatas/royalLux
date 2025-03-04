package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;

public interface UserUpdateUseCase {
    public UserUpdateUseCaseOutputDto execute (Integer id, UserUpdateUseCaseInputDto input);
}
