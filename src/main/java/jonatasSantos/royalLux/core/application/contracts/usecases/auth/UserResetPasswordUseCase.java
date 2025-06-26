package jonatasSantos.royalLux.core.application.contracts.usecases.auth;

import jonatasSantos.royalLux.core.application.models.dtos.auth.UserResetPasswordUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserResetPasswordUseCaseOutputDto;

public interface UserResetPasswordUseCase {
    public UserResetPasswordUseCaseOutputDto execute(UserResetPasswordUseCaseInputDto input);
}
