package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class PersonDeleteUseCaseImpl implements PersonDeleteUseCase {

    private final PersonRepository personRepository;

    public PersonDeleteUseCaseImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDeleteUseCaseOutputDto execute(Integer id) {
        var person = this.personRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa inexistente"));

        if(person.getUser().getRole().equals(UserRole.ADMIN))
            throw new UnauthorizedException("Admin não pode ser deletado");

        this.personRepository.delete(person);

        return new PersonDeleteUseCaseOutputDto(true);
    }
}