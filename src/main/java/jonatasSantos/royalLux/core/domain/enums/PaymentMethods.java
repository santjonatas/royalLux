package jonatasSantos.royalLux.core.domain.enums;

public enum PaymentMethods {
    DEBITO("DÉBITO"),
    CREDITO("CRÉDITO"),
    PIX("PIX");

    private final String description;

    PaymentMethods(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
