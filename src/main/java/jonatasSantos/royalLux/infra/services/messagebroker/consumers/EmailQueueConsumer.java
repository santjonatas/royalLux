package jonatasSantos.royalLux.infra.services.messagebroker.consumers;

import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.gateways.EmailGateway;
import jonatasSantos.royalLux.core.application.contracts.services.SerializerService;
import jonatasSantos.royalLux.core.application.mappers.UserAuthCodeMapper;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class EmailQueueConsumer {

    @Value("${redis.db.key.token.reset.password}")
    private String tokenResetPasswordKey;

    private final EmailGateway emailGateway;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final SerializerService serializerService;
    private final RedisTemplate<String, String> redisTemplate;

    public EmailQueueConsumer(EmailGateway emailGateway, UserRepository userRepository, PersonRepository personRepository, SerializerService serializerService, RedisTemplate<String, String> redisTemplate) {
        this.emailGateway = emailGateway;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.serializerService = serializerService;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void consumeEmail(String message) {

        var userAuthCode = serializerService.fromJson(message, UserAuthCodeMapper.class);

        var optionalUser = this.userRepository.findByUsername(userAuthCode.getUsername());

        try {
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                var person = this.personRepository.findByUserId(user.getId());

                if(!person.getEmail().isEmpty()){
                    String redisKey = tokenResetPasswordKey + "-" + userAuthCode.getUsername() + ":" + userAuthCode.getCode();

                    redisTemplate.opsForValue().set(redisKey, userAuthCode.getCode(), Duration.ofMinutes(5));

                    this.emailGateway.sendEmail(person.getEmail(), "Código de Recuperação de Senha", userAuthCode.getCode());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
    }
}
