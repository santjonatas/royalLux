package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseOutputDto;

public interface MaterialUpdateUseCase {
    public MaterialUpdateUseCaseOutputDto execute(Integer materialId, MaterialUpdateUseCaseInputDto input);
}
