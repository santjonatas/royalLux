package jonatasSantos.royalLux.infra.services.messagebroker.consumers;

import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.gateways.EmailGateway;
import jonatasSantos.royalLux.core.application.contracts.services.SerializerService;
import jonatasSantos.royalLux.core.application.mappers.UserAuthCodeMapper;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueConsumer {

    private final EmailGateway emailGateway;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final SerializerService serializerService;

    public EmailQueueConsumer(EmailGateway emailGateway, UserRepository userRepository, PersonRepository personRepository, SerializerService serializerService) {
        this.emailGateway = emailGateway;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.serializerService = serializerService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void consumeEmail(String message) {

        var userAuthCode = serializerService.fromJson(message, UserAuthCodeMapper.class);

        var optionalUser = this.userRepository.findByUsername(userAuthCode.username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            var person = this.personRepository.findByUserId(user.getId());

            if(!person.getEmail().isEmpty())
                this.emailGateway.sendEmail(person.getEmail(), "teste", "");
        }
    }
}
