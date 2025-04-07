package jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCaseOutputDto;

public interface SalonServiceCustomerServiceDeleteUseCase {
    public SalonServiceCustomerServiceDeleteUseCaseOutputDto execute(Integer id);
}
