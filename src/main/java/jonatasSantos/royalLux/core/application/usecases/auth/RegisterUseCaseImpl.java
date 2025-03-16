package jonatasSantos.royalLux.core.application.usecases.auth;

import jakarta.persistence.EntityExistsException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.RegisterUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterUseCaseImpl implements RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterUseCaseOutputDto execute(RegisterUseCaseInputDto input) {
        Optional<User> user = this.userRepository.findByUsername(input.username());

        if(!user.isEmpty()) {
            throw new EntityExistsException("Usuário já existe");
        }

        User newUser = new User(input.username(), UserRole.CLIENT, true);
        newUser.validatePassword(input.password());
        newUser.setPassword(passwordEncoder.encode(input.password()));
        this.userRepository.save(newUser);

        return new RegisterUseCaseOutputDto(newUser.getId());
    }
}