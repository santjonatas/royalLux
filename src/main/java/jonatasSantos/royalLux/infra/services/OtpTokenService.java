package jonatasSantos.royalLux.infra.services;

import jakarta.annotation.PostConstruct;
import jonatasSantos.royalLux.core.application.contracts.services.AuthCodeService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpTokenService implements AuthCodeService {

    private final Random random = new Random();
    private final Map<String, Instant> otpStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Inicialização se necessário
    }

    @Override
    public OtpPayload generateCode(int expirationSeconds) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        Instant expiresAt = Instant.now().plusSeconds(expirationSeconds);
        otpStore.put(otp, expiresAt);
        return new OtpPayload(otp);
    }

    @Override
    public boolean validate(String code) {
        Instant expiresAt = otpStore.get(code);
        if (expiresAt == null || Instant.now().isAfter(expiresAt)) {
            otpStore.remove(code); // limpa se expirado
            return false;
        }
        otpStore.remove(code); // opcional: remove após uso
        return true;
    }

    public record OtpPayload(String code) {}
}
