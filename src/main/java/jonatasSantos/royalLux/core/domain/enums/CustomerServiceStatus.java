package jonatasSantos.royalLux.core.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum CustomerServiceStatus {
    AGENDADO("AGENDADO"),
    AGUARDANDO_CONFIRMACAO("AGUARDANDO CONFIRMAÇÃO"),
    EM_ANDAMENTO("EM ANDAMENTO"),
    AGUARDANDO_CLIENTE("AGUARDANDO CLIENTE"),
    NAO_COMPARECEU("NÃO COMPARECEU"),
    FINALIZADO("FINALIZADO"),
    CANCELADO_PELO_CLIENTE("CANCELADO PELO CLIENTE"),
    CANCELADO_PELO_SALAO("CANCELADO PELO SALÃO");

    private final String descricao;

    CustomerServiceStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static final Set<CustomerServiceStatus> FINISHED_STATUSES = EnumSet.of(
            NAO_COMPARECEU,
            FINALIZADO,
            CANCELADO_PELO_CLIENTE,
            CANCELADO_PELO_SALAO
    );
}
