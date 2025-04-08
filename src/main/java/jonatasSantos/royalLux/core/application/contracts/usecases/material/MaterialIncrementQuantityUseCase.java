package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseOutputDto;

public interface MaterialIncrementQuantityUseCase {
    public MaterialIncrementQuantityUseCaseOutputDto execute(Integer materialId, MaterialIncrementQuantityUseCaseInputDto input);
}
