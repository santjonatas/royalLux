package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserUpdateUseCaseImpl implements UserUpdateUseCase {

    private final UserRepository userRepository;

    public UserUpdateUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    @Override
    public UserUpdateUseCaseOutputDto execute(User user, Integer id, UserUpdateUseCaseInputDto input){
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var userToBeUpdated = this.userRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        if (this.userRepository.existsByUsernameAndIdNot(input.username(), userToBeUpdated.getId()))
            throw new ConflictException("Nome de usuário já está em uso");

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            if(userToBeUpdated.getRole().equals(UserRole.ADMIN) && !input.role().equals(UserRole.ADMIN))
                throw new IllegalArgumentException("Admin não pode ter Permissão alterada");

            if(userToBeUpdated.getRole().equals(UserRole.ADMIN) && !input.active())
                throw new IllegalArgumentException("Admin não pode ter usuário inativo");

            if(!userToBeUpdated.getRole().equals(UserRole.ADMIN) && input.role().equals(UserRole.ADMIN))
                throw new ConflictException("Apenas um usuário pode ser Admin");

            userToBeUpdated.setUsername(input.username());
            if(!userToBeUpdated.getRole().equals(UserRole.ADMIN))
                userToBeUpdated.setRole(input.role());

            userToBeUpdated.setActive(input.active());
        }

        else if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            throw new UnauthorizedException("Você não possui autorização para atualizar usuário");
        }

        else if(userLogged.getRole().equals(UserRole.CLIENT)){
            if (userLogged.getId() != userToBeUpdated.getId())
                throw new UnauthorizedException("Você não possui autorização para atualizar outro usuário");

            if(input.role().equals(UserRole.ADMIN))
                throw new ConflictException("Apenas um usuário pode ser Admin");

            userToBeUpdated.setUsername(input.username());

            if(userToBeUpdated.isActive() != input.active())
                warningList.add("Você não possui autorização para atualizar o status de atividade");

            if(!userToBeUpdated.getRole().equals(input.role()))
                warningList.add("Você não possui autorização para atualizar a permissão");
        }

        userToBeUpdated.setUpdatedAt(LocalDateTime.now());
        this.userRepository.save(userToBeUpdated);

        return new UserUpdateUseCaseOutputDto(true, warningList);
    }
}