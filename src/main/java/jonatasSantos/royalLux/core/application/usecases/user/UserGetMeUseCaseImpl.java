package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetMeUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetMeUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserGetMeUseCaseImpl implements UserGetMeUseCase {

    private final UserRepository userRepository;

    public UserGetMeUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserGetMeUseCaseOutputDto execute(User user) {

        var myUser = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        return new UserGetMeUseCaseOutputDto(
                myUser.getId(),
                myUser.getUsername(),
                myUser.getRole(),
                myUser.isActive(),
                myUser.getCreatedAt(),
                myUser.getUpdatedAt());
    }
}