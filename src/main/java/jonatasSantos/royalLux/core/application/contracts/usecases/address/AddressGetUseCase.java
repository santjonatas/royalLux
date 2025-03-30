package jonatasSantos.royalLux.core.application.contracts.usecases.address;

import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface AddressGetUseCase {
    public List<AddressGetUseCaseOutputDto> execute(User user, AddressGetUseCaseInputDto input, Integer page, Integer size);
}
