package jonatasSantos.royalLux.infra.messagebroker.publishers;

import jonatasSantos.royalLux.core.application.contracts.services.messagebroker.publishers.MessagePublisher;
import jonatasSantos.royalLux.infra.config.RabbitMQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqPublisher implements MessagePublisher {

    private final AmqpTemplate amqpTemplate;

    public RabbitMqPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToEmailQueue(String message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DELIVERY_CHANNEL, RabbitMQConfig.ROUTING_KEY_EMAIL, message);
    }

    public void sendToWhatsappQueue(String message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DELIVERY_CHANNEL, RabbitMQConfig.ROUTING_KEY_WHATSAPP, message);
    }
}
