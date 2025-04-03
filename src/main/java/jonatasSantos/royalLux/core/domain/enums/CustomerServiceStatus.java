package jonatasSantos.royalLux.core.domain.enums;

public enum CustomerServiceStatus {
    AGENDADO("Agendado"),
    AGUARDANDO_CONFIRMACAO("Aguardando Confirmação"),
    EM_ANDAMENTO("Em Andamento"),
    AGUARDANDO_CLIENTE("Aguardando Cliente"),
    NAO_COMPARECEU("Não Compareceu"),
    FINALIZADO("Finalizado"),
    CANCELADO_PELO_CLIENTE("Cancelado pelo Cliente"),
    CANCELADO_PELO_SALAO("Cancelado pelo Salão");

    private final String descricao;

    CustomerServiceStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

