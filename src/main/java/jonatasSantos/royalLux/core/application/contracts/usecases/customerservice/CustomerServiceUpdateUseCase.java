package jonatasSantos.royalLux.core.application.contracts.usecases.customerservice;

import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface CustomerServiceUpdateUseCase {
    public CustomerServiceUpdateUseCaseOutputDto execute(User user, CustomerServiceUpdateUseCaseInputDto input);
}
