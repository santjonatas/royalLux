package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseOutputDto;

public interface MaterialDecrementQuantityUseCase {
    public MaterialDecrementQuantityUseCaseOutputDto execute(Integer materialId, MaterialDecrementQuantityUseCaseInputDto input);
}
