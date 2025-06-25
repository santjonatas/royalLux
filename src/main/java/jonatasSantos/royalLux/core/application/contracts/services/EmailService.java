package jonatasSantos.royalLux.core.application.contracts.services;

public interface EmailService {

    public void sendEmail(String to, String subject, String body);

    public void sendEmailHtml(String to, String subject, String body);

    void handle(String message);
}
