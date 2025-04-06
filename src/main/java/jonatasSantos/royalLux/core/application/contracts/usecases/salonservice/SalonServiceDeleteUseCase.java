package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;

public interface SalonServiceDeleteUseCase {
    public SalonServiceDeleteUseCaseOutputDto execute(Integer id);
}
