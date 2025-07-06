package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialUpdateUseCase {
    public MaterialUpdateUseCaseOutputDto execute(User user, Integer materialId, MaterialUpdateUseCaseInputDto input);
}
