package jonatasSantos.royalLux.core.application.contracts.services;

import jonatasSantos.royalLux.core.domain.entities.User;

public interface AuthenticationService {
    public String generateToken(User user);

    public String validateToken(String token);
}
