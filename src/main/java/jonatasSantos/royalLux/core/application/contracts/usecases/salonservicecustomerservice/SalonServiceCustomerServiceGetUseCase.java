package jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface SalonServiceCustomerServiceGetUseCase {
    public List<SalonServiceCustomerServiceGetUseCaseOutputDto> execute(User user, SalonServiceCustomerServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
