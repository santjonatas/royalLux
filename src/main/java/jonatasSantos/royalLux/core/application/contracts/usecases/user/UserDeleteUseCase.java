package jonatasSantos.royalLux.core.application.contracts.usecases.user;


import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface UserDeleteUseCase {
    public UserDeleteUseCaseOutputDto execute(User user, Integer id);
}
