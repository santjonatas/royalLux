package jonatasSantos.royalLux.core.domain.enums;

public enum PaymentStatus {
    PENDENTE("PENDENTE"),
    EM_PROCESSAMENTO("EM PROCESSAMENTO"),
    PAGO("PAGO"),
    PARCIALMENTE_PAGO("PARCIALMENTE PAGO"),
    ATRASADO("ATRASADO"),
    CANCELADO("CANCELADO"),
    REEMBOLSADO("REEMBOLSADO"),
    FALHOU("FALHOU"),
    AGENDADO("AGENDADO");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
