package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SalonServiceDeleteUseCase {
    public SalonServiceDeleteUseCaseOutputDto execute(User user, Integer id);
}
