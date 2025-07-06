package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SalonServiceUpdateUseCase {
    public SalonServiceUpdateUseCaseOutputDto execute(User user, Integer salonServiceId, SalonServiceUpdateUseCaseInputDto input);
}
