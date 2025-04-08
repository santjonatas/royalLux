package jonatasSantos.royalLux.core.application.contracts.usecases.material;

import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface MaterialGetUseCase {
    List<MaterialGetUseCaseOutputDto> execute(User user, MaterialGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
