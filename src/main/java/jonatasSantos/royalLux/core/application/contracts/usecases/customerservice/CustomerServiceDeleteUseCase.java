package jonatasSantos.royalLux.core.application.contracts.usecases.customerservice;

import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface CustomerServiceDeleteUseCase {
    public CustomerServiceDeleteUseCaseOutputDto execute(User user, Integer id);
}
