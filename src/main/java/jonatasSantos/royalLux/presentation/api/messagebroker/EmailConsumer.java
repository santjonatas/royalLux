package jonatasSantos.royalLux.presentation.api.messagebroker;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.services.EmailService;
import jonatasSantos.royalLux.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public EmailConsumer(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void consumeEmail(String message) {

        this.emailService.sendEmail("", "teste", "");

    }
}
