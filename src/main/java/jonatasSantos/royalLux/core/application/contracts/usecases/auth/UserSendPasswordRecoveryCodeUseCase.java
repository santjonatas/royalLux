package jonatasSantos.royalLux.core.application.contracts.usecases.auth;

import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseOutputDto;

public interface UserSendPasswordRecoveryCodeUseCase {

    public UserSendPasswordRecoveryCodeUseCaseOutputDto execute(UserSendPasswordRecoveryCodeUseCaseInputDto input);
}
