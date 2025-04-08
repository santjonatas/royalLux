package jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice;

import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceDeleteUseCaseOutputDto;

public interface MaterialSalonServiceDeleteUseCase {
    public MaterialSalonServiceDeleteUseCaseOutputDto execute(Integer id);
}
