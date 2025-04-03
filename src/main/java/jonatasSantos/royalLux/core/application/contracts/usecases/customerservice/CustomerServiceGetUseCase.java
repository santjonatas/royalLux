package jonatasSantos.royalLux.core.application.contracts.usecases.customerservice;

import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface CustomerServiceGetUseCase {
    public List<CustomerServiceGetUseCaseOutputDto> execute(User user, CustomerServiceGetUseCaseInputDto input, Integer page, Integer size);
}
