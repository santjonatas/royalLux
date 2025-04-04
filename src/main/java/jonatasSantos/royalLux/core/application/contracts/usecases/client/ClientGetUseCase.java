package jonatasSantos.royalLux.core.application.contracts.usecases.client;

import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface ClientGetUseCase {
    public List<ClientGetUseCaseOutputDto> execute(User user, ClientGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}