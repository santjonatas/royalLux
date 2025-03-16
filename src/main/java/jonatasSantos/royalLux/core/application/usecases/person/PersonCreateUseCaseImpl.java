package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonCreateUseCaseImpl implements PersonCreateUseCase {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonCreateUseCaseImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PersonCreateUseCaseOutputDto execute(PersonCreateUseCaseInputDto input) {
        var user = this.userRepository.findById(String.valueOf(input.userId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário é inexistente"));

        Person person = new Person(user,
                input.name(),
                input.dateBirth(),
                input.cpf(),
                input.phone(),
                input.email());

        personRepository.save(person);

        return new PersonCreateUseCaseOutputDto(person.getId());
    }
}
