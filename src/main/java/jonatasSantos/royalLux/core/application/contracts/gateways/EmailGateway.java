package jonatasSantos.royalLux.core.application.contracts.gateways;

import java.io.IOException;

public interface EmailGateway {

    public void sendEmail(String to, String subject, String body);

    public void sendEmailHtml(String to, String subject, String body);

    void handle(String message);

    public String loadTemplate(String path) throws IOException;

    public void sendOtpCodeResetPassword(String to, String subject, String otpCode, String username, String body) throws IOException;
}
