package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityExistsException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCreateUseCaseImpl implements UserCreateUseCase{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreateUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @AuditLogAnnotation
    @Override
    public UserCreateUseCaseOutputDto execute(User user, UserCreateUseCaseInputDto input) {
        Optional<User> userToBeCreated = this.userRepository.findByUsername(input.username());

        if(!userToBeCreated.isEmpty())
            throw new EntityExistsException("Usuário já existe");

        if(input.role().equals(UserRole.ADMIN))
            throw new IllegalArgumentException("Apenas um usuário pode ser Admin");

        User newUser = new User(input.username(), input.role(), input.active());
        newUser.validatePassword(input.password());
        newUser.setPassword(passwordEncoder.encode(input.password()));
        this.userRepository.save(newUser);

        return new UserCreateUseCaseOutputDto(newUser.getId());
    }
}