package jonatasSantos.royalLux.core.application.contracts.usecases.address;

import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface AddressUpdateUseCase {
    public AddressUpdateUseCaseOutputDto execute(User user, Integer personId, AddressUpdateUseCaseInputDto input);
}
