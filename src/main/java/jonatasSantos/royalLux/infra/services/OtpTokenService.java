package jonatasSantos.royalLux.infra.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jonatasSantos.royalLux.core.application.contracts.services.AuthCodeService;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class OtpTokenService implements AuthCodeService {

    private Key key;
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public OtpPayload generateCode(int expirationSeconds) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);

        String token = Jwts.builder()
                .claim("otp", otp)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(key)
                .compact();

        return new OtpPayload(otp, token);
    }

    public boolean validate(String token, String code) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenOtp = claims.get("otp", String.class);
            return tokenOtp.equals(code);
        } catch (JwtException e) {
            return false;
        }
    }

    public record OtpPayload(String code, String token) {}
}
