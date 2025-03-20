package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonDeleteUseCaseOutputDto;

public interface PersonDeleteUseCase {
    public PersonDeleteUseCaseOutputDto execute(Integer id);
}
