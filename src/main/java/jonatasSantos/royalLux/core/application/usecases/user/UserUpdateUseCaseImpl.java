package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserUpdateUseCaseImpl implements UserUpdateUseCase {

    private final UserRepository userRepository;

    public UserUpdateUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    @Override
    public UserUpdateUseCaseOutputDto execute(User user, Integer id, UserUpdateUseCaseInputDto input) throws RoleNotFoundException {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId())).orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var userToBeUpdated = this.userRepository.findById(id.toString()).orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        if(!UserRole.ROLES.contains(userLogged.getRole()))
            throw new RoleNotFoundException("Permissão inexistente");

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            boolean usernameExists = this.userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId());

            if (usernameExists)
                throw new IllegalArgumentException("Nome de usuário já está em uso");

            if(userToBeUpdated.getRole().equals(UserRole.ADMIN) && !input.role().equals(UserRole.ADMIN))
                throw new IllegalArgumentException("Admin não pode ter Permissão alterada");

            if(userToBeUpdated.getRole().equals(UserRole.ADMIN) && input.active() == false)
                throw new IllegalArgumentException("Admin não pode ter usuário inativo");

            userToBeUpdated.setUsername(input.username());
            if(!userToBeUpdated.getRole().equals(UserRole.ADMIN))
                userToBeUpdated.setRole(input.role());

            userToBeUpdated.setActive(input.active());
            userToBeUpdated.setUpdatedAt(LocalDateTime.now());
        }

        else if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            throw new UnauthorizedException("Você não possui autorização para atualizar usuário");
        }

        else if(userLogged.getRole().equals(UserRole.CLIENT)){
            boolean usernameExists = this.userRepository.existsByUsernameAndIdNot(input.username(), userLogged.getId());

            if (usernameExists)
                throw new IllegalArgumentException("Nome de usuário já está em uso");

            if (userLogged.getId() != userToBeUpdated.getId())
                throw new UnauthorizedException("Você não possui autorização para atualizar outro usuário");

            userToBeUpdated.setUsername(input.username());
            userToBeUpdated.setUpdatedAt(LocalDateTime.now());

            if(userToBeUpdated.isActive() != input.active())
                warningList.add("Você não possui autorização para atualizar o status de atividade");

            if(!userToBeUpdated.getRole().equals(input.role()))
                warningList.add("Você não possui autorização para atualizar a permissão");
        }

        this.userRepository.save(userToBeUpdated);

        return new UserUpdateUseCaseOutputDto(true, warningList);
    }
}