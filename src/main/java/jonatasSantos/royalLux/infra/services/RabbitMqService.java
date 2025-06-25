package jonatasSantos.royalLux.infra.services;

import jonatasSantos.royalLux.core.application.contracts.services.MessagePublisher;
import jonatasSantos.royalLux.infra.config.RabbitMQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService implements MessagePublisher {

    private final AmqpTemplate amqpTemplate;

    public RabbitMqService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToEmailQueue(String message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DELIVERY_CHANNEL, RabbitMQConfig.ROUTING_KEY_EMAIL, message);
    }

    public void sendToWhatsappQueue(String message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DELIVERY_CHANNEL, RabbitMQConfig.ROUTING_KEY_WHATSAPP, message);
    }
}
