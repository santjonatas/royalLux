package jonatasSantos.royalLux.core.application.contracts.services.messagebroker.publishers;

public interface MessagePublisher {

    void sendToEmailQueue(String message);

    void sendToWhatsappQueue(String message);
}
