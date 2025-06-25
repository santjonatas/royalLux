package jonatasSantos.royalLux.core.application.contracts.services;


import jonatasSantos.royalLux.infra.services.OtpTokenService;

public interface AuthCodeService {

    public OtpTokenService.OtpPayload generateCode(int expirationSeconds);

    public boolean validate(String token, String code);
}
