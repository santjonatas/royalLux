package jonatasSantos.royalLux.core.application.usecases.person;

import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class PersonUpdateUseCaseImpl implements PersonUpdateUseCase {
    @Override
    public PersonUpdateUseCaseOutputDto execute(User user, Integer userId, PersonUpdateUseCaseInputDto input) {
        return null;
    }
}
