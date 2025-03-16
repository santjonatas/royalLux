package jonatasSantos.royalLux.core.application.usecases.user;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetAllUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetAllUseCaseOutputDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGetAllUseCaseImpl implements UserGetAllUseCase {

    private final UserRepository userRepository;

    public UserGetAllUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserGetAllUseCaseOutputDto> execute() {
        var users = this.userRepository.findAll();

        return users.stream()
                .sorted((u1, u2) -> Long.compare(u2.getId(), u1.getId()))
                .map(user -> new UserGetAllUseCaseOutputDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.isActive()))
                .collect(Collectors.toList());
    }
}
