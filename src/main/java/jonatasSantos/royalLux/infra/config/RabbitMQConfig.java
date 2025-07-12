package jonatasSantos.royalLux.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_DELIVERY_CHANNEL = "deliveryChannelExchange";
    public static final String QUEUE_EMAIL = "emailQueue";
    public static final String ROUTING_KEY_EMAIL = "emailRoutingKey";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DELIVERY_CHANNEL);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_EMAIL, true);
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(emailQueue).to(directExchange).with(ROUTING_KEY_EMAIL);
    }
}
