package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class PersonDeleteUseCaseImpl implements PersonDeleteUseCase {

    private final PersonRepository personRepository;

    public PersonDeleteUseCaseImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @AuditLogAnnotation
    @Override
    public PersonDeleteUseCaseOutputDto execute(User user, Integer id) {
        var person = this.personRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Dados pessoais inexistentes"));

        if(person.getUser().getRole().equals(UserRole.ADMIN))
            throw new UnauthorizedException("Admin não pode ser deletado");

        this.personRepository.delete(person);

        return new PersonDeleteUseCaseOutputDto(true);
    }
}