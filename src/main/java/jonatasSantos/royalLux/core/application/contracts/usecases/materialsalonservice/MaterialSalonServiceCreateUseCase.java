package jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice;

import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialSalonServiceCreateUseCase {
    public MaterialSalonServiceCreateUseCaseOutputDto execute(User user, MaterialSalonServiceCreateUseCaseInputDto input);
}
