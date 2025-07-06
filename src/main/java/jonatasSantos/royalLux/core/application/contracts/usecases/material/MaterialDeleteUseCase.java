package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialDeleteUseCase {
    public MaterialDeleteUseCaseOutputDto execute(User user, Integer id);
}
