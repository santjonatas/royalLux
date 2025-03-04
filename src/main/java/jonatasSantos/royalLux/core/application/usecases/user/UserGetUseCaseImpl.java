package jonatasSantos.royalLux.core.application.usecases.user;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGetUseCaseImpl implements UserGetUseCase {

    private final UserRepository userRepository;

    public UserGetUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserGetUseCaseOutputDto execute(Integer id) {
        Optional<User> userOptional = this.userRepository.findById(id.toString());

        return userOptional.map(user ->
                new UserGetUseCaseOutputDto(user.getId(), user.getUsername(), user.isActive())
        ).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
