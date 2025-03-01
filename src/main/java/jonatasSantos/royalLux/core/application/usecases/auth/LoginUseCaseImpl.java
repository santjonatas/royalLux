package jonatasSantos.royalLux.core.application.usecases.auth;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.services.AuthenticationService;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.LoginUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public LoginUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public LoginUseCaseOutputDto execute(LoginUseCaseInputDto input) throws AuthenticationException {
        User user = this.userRepository.findByUsername(input.username()).orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha inválidos"));
        if(!passwordEncoder.matches(input.password(), user.getPassword())) {
            throw new AuthenticationException("Usuário ou senha inválidos");
        }

        String token = this.authenticationService.generateToken(user);
        return new LoginUseCaseOutputDto(token);
    }
}
