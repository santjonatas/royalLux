package jonatasSantos.royalLux.core.domain.enums;

public enum PaymentMethod {
    DEBITO("DÉBITO"),
    CREDITO("CRÉDITO"),
    PIX("PIX"),
    DINHEIRO("DINHEIRO");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
