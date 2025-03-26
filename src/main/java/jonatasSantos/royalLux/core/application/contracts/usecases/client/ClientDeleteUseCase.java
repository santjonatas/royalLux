package jonatasSantos.royalLux.core.application.contracts.usecases.client;

import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;

public interface ClientDeleteUseCase {
    public ClientDeleteUseCaseOutputDto execute(Integer id);
}