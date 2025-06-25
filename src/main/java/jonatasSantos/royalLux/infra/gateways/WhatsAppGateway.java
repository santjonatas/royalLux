package jonatasSantos.royalLux.infra.gateways;

import com.fasterxml.jackson.databind.ObjectMapper;
import jonatasSantos.royalLux.core.application.mappers.WhatsAppMessageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class WhatsAppGateway {

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.phone.id}")
    private String phoneNumberId;

    @Value("${whatsapp.token}")
    private String token;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(String numeroDestino, String mensagem) throws Exception {
        URL url = new URL(apiUrl + phoneNumberId + "/messages");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        WhatsAppMessageMapper body = new WhatsAppMessageMapper(numeroDestino, mensagem);
        String jsonBody = objectMapper.writeValueAsString(body);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = connection.getResponseCode();
    }
}
