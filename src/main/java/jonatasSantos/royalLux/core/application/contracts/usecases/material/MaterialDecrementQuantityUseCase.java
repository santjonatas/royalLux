package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialDecrementQuantityUseCase {
    public MaterialDecrementQuantityUseCaseOutputDto execute(User user, Integer materialId, MaterialDecrementQuantityUseCaseInputDto input);
}
