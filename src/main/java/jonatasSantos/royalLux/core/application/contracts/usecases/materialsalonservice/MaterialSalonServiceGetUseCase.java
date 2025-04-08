package jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice;

import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface MaterialSalonServiceGetUseCase {
    List<MaterialSalonServiceGetUseCaseOutputDto> execute(User user, MaterialSalonServiceGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
