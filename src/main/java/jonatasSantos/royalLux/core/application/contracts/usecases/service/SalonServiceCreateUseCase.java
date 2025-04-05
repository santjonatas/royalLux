package jonatasSantos.royalLux.core.application.contracts.usecases.service;

import jonatasSantos.royalLux.core.application.models.dtos.service.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.service.SalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SalonServiceCreateUseCase {
    public SalonServiceCreateUseCaseOutputDto execute(User user, SalonServiceCreateUseCaseInputDto input);
}
