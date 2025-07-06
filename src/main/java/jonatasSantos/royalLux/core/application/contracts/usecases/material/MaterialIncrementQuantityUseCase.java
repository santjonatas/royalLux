package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialIncrementQuantityUseCase {
    public MaterialIncrementQuantityUseCaseOutputDto execute(User user, Integer materialId, MaterialIncrementQuantityUseCaseInputDto input);
}
