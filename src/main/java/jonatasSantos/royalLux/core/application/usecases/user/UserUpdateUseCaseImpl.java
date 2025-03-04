package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateUseCaseImpl implements UserUpdateUseCase {

    private final UserRepository userRepository;

    public UserUpdateUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserUpdateUseCaseOutputDto execute(Integer id, UserUpdateUseCaseInputDto input) {
        var user = this.userRepository.findById(id.toString()).orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        user.setUsername(input.username());
        user.setPassword(input.password());
        user.setActive(input.active());

        this.userRepository.save(user);

        return new UserUpdateUseCaseOutputDto(true);
    }
}
