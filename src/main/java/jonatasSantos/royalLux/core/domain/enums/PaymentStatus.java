package jonatasSantos.royalLux.core.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum PaymentStatus {
    PENDENTE("PENDENTE"),
    EM_PROCESSAMENTO("EM PROCESSAMENTO"),
    PAGO("PAGO"),
    CANCELADO("CANCELADO"),
    EXTORNADO("EXTORNADO"),
    REEMBOLSADO("REEMBOLSADO"),
    FALHOU("FALHOU");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static final List<PaymentStatus> CREDIT_CARD_STATUSES = Arrays.asList(
            PaymentStatus.PENDENTE,
            PaymentStatus.EM_PROCESSAMENTO,
            PaymentStatus.PAGO,
            PaymentStatus.CANCELADO,
            PaymentStatus.EXTORNADO,
            PaymentStatus.REEMBOLSADO,
            PaymentStatus.FALHOU
    );

    public static final List<PaymentStatus> DEBIT_CARD_STATUSES = Arrays.asList(
            PaymentStatus.PENDENTE,
            PaymentStatus.EM_PROCESSAMENTO,
            PaymentStatus.PAGO,
            PaymentStatus.CANCELADO,
            PaymentStatus.EXTORNADO,
            PaymentStatus.REEMBOLSADO,
            PaymentStatus.FALHOU
    );

    public static final List<PaymentStatus> PIX_STATUSES = Arrays.asList(
            PaymentStatus.PENDENTE,
            PaymentStatus.EM_PROCESSAMENTO,
            PaymentStatus.PAGO,
            PaymentStatus.CANCELADO,
            PaymentStatus.REEMBOLSADO,
            PaymentStatus.FALHOU
    );

    public static final List<PaymentStatus> CASH_STATUSES = Arrays.asList(
            PaymentStatus.PENDENTE,
            PaymentStatus.PAGO,
            PaymentStatus.CANCELADO,
            PaymentStatus.REEMBOLSADO
    );
}
