package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetAllUseCaseOutputDto;

import java.util.List;

public interface UserGetAllUseCase {
    public List<UserGetAllUseCaseOutputDto> execute();
}
