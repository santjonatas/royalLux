package jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice;

import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialSalonServiceUpdateUseCase {
    public MaterialSalonServiceUpdateUseCaseOutputDto execute(User user, Integer materialSalonServiceId, MaterialSalonServiceUpdateUseCaseInputDto input);
}
