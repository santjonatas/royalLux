package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SalonServiceCreateUseCase {
    public SalonServiceCreateUseCaseOutputDto execute(User user, SalonServiceCreateUseCaseInputDto input);
}
