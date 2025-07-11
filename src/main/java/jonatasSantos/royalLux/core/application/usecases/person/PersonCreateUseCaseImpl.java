package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Person;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class PersonCreateUseCaseImpl implements PersonCreateUseCase {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonCreateUseCaseImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public PersonCreateUseCaseOutputDto execute(User user, PersonCreateUseCaseInputDto input){
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var existingUser = this.userRepository.findById(String.valueOf(input.userId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário é inexistente"));

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            if(!existingUser.getRole().equals(UserRole.CLIENT))
                throw new UnauthorizedException("Você não possui autorização para criar dados pessoais de usuário com outra permissão");

            if(existingUser.getId() != userLogged.getId())
                throw new UnauthorizedException("Você não possui autorização para criar dados pessoais de outro usuário");
        }

        if(this.personRepository.existsByUserId(existingUser.getId()))
            throw new ConflictException("Dados pessoais já vinculados a um usuário");

        if(this.personRepository.existsByCpf(input.cpf()))
            throw new ConflictException("CPF já vinculado a um usuário");

        if(this.personRepository.existsByPhone(input.phone()))
            throw new ConflictException("Telefone já vinculado a um usuário");

        if(this.personRepository.existsByEmail(input.email()))
            throw new ConflictException("Email já vinculado a um usuário");

        Person person = new Person(
                existingUser,
                input.name(),
                input.dateBirth(),
                input.cpf(),
                input.phone(),
                input.email());

        this.personRepository.save(person);

        return new PersonCreateUseCaseOutputDto(person.getId());
    }
}
