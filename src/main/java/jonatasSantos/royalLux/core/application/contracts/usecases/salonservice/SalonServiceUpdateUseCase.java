package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseOutputDto;

public interface SalonServiceUpdateUseCase {
    public SalonServiceUpdateUseCaseOutputDto execute(Integer salonServiceId, SalonServiceUpdateUseCaseInputDto input);
}
