package jonatasSantos.royalLux.core.application.contracts.usecases.address;

import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface AddressCreateUseCase {
    public AddressCreateUseCaseOutputDto execute(User user, AddressCreateUseCaseInputDto input);
}