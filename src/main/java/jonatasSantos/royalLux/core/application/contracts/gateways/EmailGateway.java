package jonatasSantos.royalLux.core.application.contracts.gateways;

public interface EmailGateway {

    public void sendEmail(String to, String subject, String body);

    public void sendEmailHtml(String to, String subject, String body);

    void handle(String message);
}
