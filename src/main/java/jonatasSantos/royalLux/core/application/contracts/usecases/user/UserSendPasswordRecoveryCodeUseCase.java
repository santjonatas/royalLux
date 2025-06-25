package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.user.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserSendPasswordRecoveryCodeUseCaseOutputDto;

public interface UserSendPasswordRecoveryCodeUseCase {

    public UserSendPasswordRecoveryCodeUseCaseOutputDto execute(UserSendPasswordRecoveryCodeUseCaseInputDto input);
}
