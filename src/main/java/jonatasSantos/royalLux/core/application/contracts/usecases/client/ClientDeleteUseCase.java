package jonatasSantos.royalLux.core.application.contracts.usecases.client;

import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface ClientDeleteUseCase {
    public ClientDeleteUseCaseOutputDto execute(User user, Integer id);
}