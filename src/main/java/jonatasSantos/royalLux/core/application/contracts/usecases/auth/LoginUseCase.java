package jonatasSantos.royalLux.core.application.contracts.usecases.auth;

import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseOutputDto;

import javax.naming.AuthenticationException;

public interface LoginUseCase {
    public LoginUseCaseOutputDto execute(LoginUseCaseInputDto input) throws AuthenticationException;
}
