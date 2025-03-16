package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateUseCaseImpl implements UserUpdateUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUpdateUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserUpdateUseCaseOutputDto execute(Integer id, UserUpdateUseCaseInputDto input) {
        var user = this.userRepository.findById(id.toString()).orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        boolean usernameExists = this.userRepository.existsByUsernameAndIdNot(input.username(), user.getId());

        if (usernameExists)
            throw new IllegalArgumentException("Nome de usuário já está em uso.");

        user.setUsername(input.username());
        user.validatePassword(input.password());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setActive(input.active());

        this.userRepository.save(user);

        return new UserUpdateUseCaseOutputDto(true);
    }
}
