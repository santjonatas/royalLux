package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialCreateUseCase {
    public MaterialCreateUseCaseOutputDto execute(User user, MaterialCreateUseCaseInputDto input);
}
