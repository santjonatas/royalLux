package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
public class UserUpdateUseCaseImpl implements UserUpdateUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUpdateUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserUpdateUseCaseOutputDto execute(User user, Integer id, UserUpdateUseCaseInputDto input) throws RoleNotFoundException {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId())).orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var userToBeUpdated = this.userRepository.findById(id.toString()).orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        boolean usernameExists = this.userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId());

        if (usernameExists)
            throw new IllegalArgumentException("Nome de usuário já está em uso");

        if(!UserRole.ROLES.contains(userLogged.getRole()))
            throw new RoleNotFoundException("Permissão inexistente");

        if(userLogged.getRole().equals(UserRole.ADMIN)){

        }

        else if(userLogged.getRole().equals(UserRole.EMPLOYEE)){

        }

        else if(userLogged.getRole().equals(UserRole.CLIENT)){

        }

        this.userRepository.save(userToBeUpdated);

        return new UserUpdateUseCaseOutputDto(true);
    }
}
