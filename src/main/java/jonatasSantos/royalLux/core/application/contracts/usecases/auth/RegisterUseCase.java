package jonatasSantos.royalLux.core.application.contracts.usecases.auth;

import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseOutputDto;

public interface RegisterUseCase {
    public RegisterUseCaseOutputDto execute(RegisterUseCaseInputDto input);
}
