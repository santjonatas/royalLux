package jonatasSantos.royalLux.core.application.contracts.services;

public interface MessagePublisher {

    void sendToEmailQueue(String message);

    void sendToWhatsappQueue(String message);
}
