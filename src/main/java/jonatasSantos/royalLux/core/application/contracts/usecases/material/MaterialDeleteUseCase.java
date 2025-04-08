package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;

public interface MaterialDeleteUseCase {
    public MaterialDeleteUseCaseOutputDto execute(Integer id);
}
