package jonatasSantos.royalLux.core.application.contracts.usecases.customerservice;

import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface CustomerServiceCreateUseCase {
    public CustomerServiceCreateUseCaseOutputDto execute(User user, CustomerServiceCreateUseCaseInputDto input);
}
