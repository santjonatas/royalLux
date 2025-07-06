package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PersonDeleteUseCase {
    public PersonDeleteUseCaseOutputDto execute(User user, Integer id);
}