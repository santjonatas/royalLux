package jonatasSantos.royalLux.infra.data;

import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.domain.entities.Person;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import jonatasSantos.royalLux.core.domain.entities.SchedulerConfig;
import jonatasSantos.royalLux.core.application.contracts.repositories.SchedulerConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.birth}")
    private String adminBirth;

    @Value("${admin.cpf}")
    private String adminCpf;

    @Value("${admin.phone}")
    private String adminPhone;

    @Value("${admin.email}")
    private String adminEmail;


    @Value("${scheduler.name.audit.log.delete}")
    private String auditLogDeleteSchedulerName;

    @Value("${scheduler.description.audit.log.delete}")
    private String auditLogDeleteSchedulerDescription;

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final SchedulerConfigRepository schedulerConfigRepository;

    public DatabaseSeeder(UserRepository userRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder, SchedulerConfigRepository schedulerConfigRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.schedulerConfigRepository = schedulerConfigRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            this.createAdmin();

            System.out.println("✅ Seed de usuários inserido com sucesso!");
        }

        var auditLogDeleteScheduler = this.schedulerConfigRepository.findByName(auditLogDeleteSchedulerName);
        if(auditLogDeleteScheduler == null) {
            this.createAuditLogDeleteSchedulerConfig();

            System.out.println("✅ Seed de configurações de scheduler Log Auditoria inserido com sucesso!");
        }
    }

    public void createAdmin(){
        User userAdmin = new User(this.adminUsername, UserRole.ADMIN, true);
        userAdmin.validatePassword(this.adminPassword);
        userAdmin.setPassword(passwordEncoder.encode(this.adminPassword));
        this.userRepository.save(userAdmin);

        Person personAdmin = new Person(userAdmin, this.adminName, LocalDate.parse(this.adminBirth), this.adminCpf, this.adminPhone, this.adminEmail);
        this.personRepository.save(personAdmin);
    }

    public void createAuditLogDeleteSchedulerConfig(){
        SchedulerConfig schedulerConfig = new SchedulerConfig(auditLogDeleteSchedulerName, auditLogDeleteSchedulerDescription, null, false);
        this.schedulerConfigRepository.save(schedulerConfig);
    }
}