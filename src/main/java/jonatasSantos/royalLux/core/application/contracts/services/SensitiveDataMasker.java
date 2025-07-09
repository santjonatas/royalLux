package jonatasSantos.royalLux.core.application.contracts.services;

public interface SensitiveDataMasker {
    public String maskSensitiveFields(String jsonStr);
}
