package jonatasSantos.royalLux.infra.data;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User("santjonatas_", passwordEncoder.encode("Teste123@"), UserRole.ADMIN, true);

            userRepository.saveAll(List.of(admin));
            System.out.println("✅ Seed de usuários inserido com sucesso!");
        }
    }
}