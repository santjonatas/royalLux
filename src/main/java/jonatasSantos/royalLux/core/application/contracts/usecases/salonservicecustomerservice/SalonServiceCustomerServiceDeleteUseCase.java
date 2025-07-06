package jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SalonServiceCustomerServiceDeleteUseCase {
    public SalonServiceCustomerServiceDeleteUseCaseOutputDto execute(User user, Integer id);
}
