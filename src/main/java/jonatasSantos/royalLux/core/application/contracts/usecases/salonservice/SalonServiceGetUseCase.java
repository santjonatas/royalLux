package jonatasSantos.royalLux.core.application.contracts.usecases.salonservice;

import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface SalonServiceGetUseCase {
    public List<SalonServiceGetUseCaseOutputDto> execute(User user, SalonServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
