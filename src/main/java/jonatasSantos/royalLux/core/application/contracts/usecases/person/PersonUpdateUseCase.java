package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PersonUpdateUseCase {
    public PersonUpdateUseCaseOutputDto execute(User user, Integer userId, PersonUpdateUseCaseInputDto input);
}
