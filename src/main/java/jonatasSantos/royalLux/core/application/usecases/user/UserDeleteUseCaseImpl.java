package jonatasSantos.royalLux.core.application.usecases.user;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ResourceNotFoundException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteUseCaseImpl implements UserDeleteUseCase {

    private final UserRepository userRepository;

    public UserDeleteUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDeleteUseCaseOutputDto execute(Integer id) {
        var user = this.userRepository.findById(id.toString()).orElseThrow(() -> new ResourceNotFoundException("Usuário inexistente"));

        this.userRepository.delete(user);

        return new UserDeleteUseCaseOutputDto(true);
    }
}
