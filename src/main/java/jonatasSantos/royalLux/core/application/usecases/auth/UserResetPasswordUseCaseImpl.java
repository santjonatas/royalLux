package jonatasSantos.royalLux.core.application.usecases.auth;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.services.AuthCodeService;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.UserResetPasswordUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserResetPasswordUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserResetPasswordUseCaseOutputDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserResetPasswordUseCaseImpl implements UserResetPasswordUseCase {

    @Value("${redis.db.key.token.reset.password}")
    private String tokenResetPasswordKey;

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthCodeService authCodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResetPasswordUseCaseImpl(RedisTemplate<String, String> redisTemplate, AuthCodeService authCodeService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.redisTemplate = redisTemplate;
        this.authCodeService = authCodeService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResetPasswordUseCaseOutputDto execute(UserResetPasswordUseCaseInputDto input) {

        String redisKey = tokenResetPasswordKey + "-" + input.username() + ":" + input.code();
        String token = redisTemplate.opsForValue().get(redisKey);

        if (token == null || !authCodeService.validate(input.code())) {
            throw new CredentialsExpiredException("Código inválido ou expirado");
        }

        var user = userRepository.findByUsername(input.username())
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        user.validatePassword(input.newPassword());
        user.setPassword(passwordEncoder.encode(input.newPassword()));
        this.userRepository.save(user);

        return new UserResetPasswordUseCaseOutputDto(true);
    }
}
