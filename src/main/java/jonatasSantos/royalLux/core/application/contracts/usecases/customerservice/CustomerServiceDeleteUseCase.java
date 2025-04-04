package jonatasSantos.royalLux.core.application.contracts.usecases.customerservice;

import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceDeleteUseCaseOutputDto;

public interface CustomerServiceDeleteUseCase {
    public CustomerServiceDeleteUseCaseOutputDto execute(Integer id);
}
