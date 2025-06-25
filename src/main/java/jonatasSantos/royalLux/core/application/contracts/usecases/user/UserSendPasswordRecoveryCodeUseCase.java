package jonatasSantos.royalLux.core.application.contracts.usecases.user;

import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseOutputDto;

public interface UserSendPasswordRecoveryCodeUseCase {

    public UserSendPasswordRecoveryCodeUseCaseOutputDto execute(UserSendPasswordRecoveryCodeUseCaseInputDto input);
}
