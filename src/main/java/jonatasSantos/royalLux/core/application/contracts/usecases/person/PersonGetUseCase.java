package jonatasSantos.royalLux.core.application.contracts.usecases.person;

import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface PersonGetUseCase {
    public List<PersonGetUseCaseOutputDto> execute(User user, PersonGetUseCaseInputDto input, Integer page, Integer size);
}