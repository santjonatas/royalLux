package jonatasSantos.royalLux.core.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum SalonServicesCustomerServiceStatus {
    PENDENTE("PENDENTE"),
    REALIZADO("REALIZADO"),
    CANCELADO("CANCELADO");

    private final String descricao;

    SalonServicesCustomerServiceStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static final Set<SalonServicesCustomerServiceStatus> FINISHED_STATUS = EnumSet.of(
            CANCELADO,
            REALIZADO
    );
}
