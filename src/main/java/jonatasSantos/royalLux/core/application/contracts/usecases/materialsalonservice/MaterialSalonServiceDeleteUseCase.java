package jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice;

import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface MaterialSalonServiceDeleteUseCase {
    public MaterialSalonServiceDeleteUseCaseOutputDto execute(User user, Integer id);
}
