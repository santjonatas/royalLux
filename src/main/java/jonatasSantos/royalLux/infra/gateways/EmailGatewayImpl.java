package jonatasSantos.royalLux.infra.gateways;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jonatasSantos.royalLux.core.application.contracts.gateways.EmailGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailGatewayImpl implements EmailGateway {

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.sendername}")
    private String senderName;

    private final JavaMailSender mailSender;

    public EmailGatewayImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void send(String to, String subject, String body, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(String.format("%s <%s>", senderName, senderEmail));

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        send(to, subject, body, false);
    }

    public void sendEmailHtml(String to, String subject, String body) {
        send(to, subject, body, true);
    }

    @Override
    public void handle(String messageContent) {
        System.out.println("Recebido da fila de email: " + messageContent);
    }

    public String loadTemplate(String path) throws IOException {
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }

    public void sendOtpCodeResetPassword(String to, String subject, String otpCode, String username, String body) throws IOException {
        String htmlTemplate = loadTemplate(body);

        htmlTemplate = htmlTemplate
                .replace("{{MOTIVO_ENVIO}}", "redefinição de senha")
                .replace("{{OTP_CODE}}", otpCode)
                .replace("{{NOME_USUARIO}}", username);

        sendEmailHtml(to, subject, htmlTemplate);
    }
}
