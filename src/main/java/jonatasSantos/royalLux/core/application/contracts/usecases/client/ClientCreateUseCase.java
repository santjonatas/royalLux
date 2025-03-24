package jonatasSantos.royalLux.core.application.contracts.usecases.client;

import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface ClientCreateUseCase {
    public ClientCreateUseCaseOutputDto execute(User user, ClientCreateUseCaseInputDto input);
}