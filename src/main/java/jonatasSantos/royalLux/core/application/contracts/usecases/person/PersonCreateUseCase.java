package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseOutputDto;

public interface PersonCreateUseCase {
    public PersonCreateUseCaseOutputDto execute(PersonCreateUseCaseInputDto input);
}
