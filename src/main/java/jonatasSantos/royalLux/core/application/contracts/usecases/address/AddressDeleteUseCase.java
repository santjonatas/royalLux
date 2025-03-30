package jonatasSantos.royalLux.core.application.contracts.usecases.address;

import jonatasSantos.royalLux.core.application.models.dtos.address.AddressDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface AddressDeleteUseCase {
    public AddressDeleteUseCaseOutputDto execute(User user, Integer id);
}
