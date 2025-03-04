package jonatasSantos.royalLux.core.application.contracts.usecases.user;


import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;

public interface UserDeleteUseCase {
    public UserDeleteUseCaseOutputDto execute(Integer id);
}
