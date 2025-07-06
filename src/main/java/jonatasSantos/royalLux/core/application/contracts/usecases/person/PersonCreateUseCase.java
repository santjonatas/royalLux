package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PersonCreateUseCase {
    public PersonCreateUseCaseOutputDto execute(User user, PersonCreateUseCaseInputDto input);
}
