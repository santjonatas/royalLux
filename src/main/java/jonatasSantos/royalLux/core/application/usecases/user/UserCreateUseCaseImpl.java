package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityExistsException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;
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

    @Override
    public UserCreateUseCaseOutputDto execute(UserCreateUseCaseInputDto input) {
        Optional<User> user = this.userRepository.findByUsername(input.username());

        if(!user.isEmpty())
            throw new EntityExistsException("Usuário já existe");

        User newUser = new User(input.username(), input.role(), input.active());
        newUser.validatePassword(input.password());
        newUser.setPassword(passwordEncoder.encode(input.password()));
        this.userRepository.save(newUser);

        return new UserCreateUseCaseOutputDto(newUser.getId());
    }
}