package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PersonUpdateUseCaseImpl implements PersonUpdateUseCase {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonUpdateUseCaseImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PersonUpdateUseCaseOutputDto execute(User user, Integer personId, PersonUpdateUseCaseInputDto input) {
        var userLogged = userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var personToBeUpdated = this.personRepository.findById(String.valueOf(personId))
                .orElseThrow(() -> new EntityNotFoundException("Pessoa é inexistente"));

        if(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
            throw new ConflictException("CPF já vinculado a um usuário");

        if(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
            throw new ConflictException("Telefone já vinculado a um usuário");

        if(personRepository.existsByEmailAndIdNot(input.email(), personToBeUpdated.getId()))
            throw new ConflictException("Email já vinculado a um usuário");

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            personToBeUpdated.setName(input.name());
            personToBeUpdated.setDateBirth(input.dateBirth());
            personToBeUpdated.setCpf(input.cpf());
            personToBeUpdated.setPhone(input.phone());
            personToBeUpdated.setEmail(input.email());
        }

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            if(personToBeUpdated.getUser().getId() != userLogged.getId())
                throw new UnauthorizedException("Você não possui autorização para atualizar outra pessoa");

            personToBeUpdated.setPhone(input.phone());
            personToBeUpdated.setEmail(input.email());

            if(!personToBeUpdated.getName().equals(input.name()))
                warningList.add("Você não possui autorização para atualizar o nome");

            if(!personToBeUpdated.getDateBirth().equals(input.dateBirth()))
                warningList.add("Você não possui autorização para atualizar a data de nascimento");

            if(!personToBeUpdated.getCpf().equals(input.cpf()))
                warningList.add("Você não possui autorização para atualizar o CPF");
        }

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            if(personToBeUpdated.getUser().getId() != userLogged.getId())
                throw new UnauthorizedException("Você não possui autorização para atualizar outra pessoa");

            personToBeUpdated.setName(input.name());
            personToBeUpdated.setDateBirth(input.dateBirth());
            personToBeUpdated.setCpf(input.cpf());
            personToBeUpdated.setPhone(input.phone());
            personToBeUpdated.setEmail(input.email());
        }

        personToBeUpdated.setUpdatedAt(LocalDateTime.now());
        personRepository.save(personToBeUpdated);

        return new PersonUpdateUseCaseOutputDto(true, warningList);
    }
}