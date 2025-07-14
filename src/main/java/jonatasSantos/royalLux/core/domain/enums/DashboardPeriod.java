package jonatasSantos.royalLux.core.domain.enums;

public enum DashboardPeriod {
    DIARIO("DIÁRIO"),
    SEMANAL("SEMANAL"),
    MENSAL("MENSAL");

    private final String description;

    DashboardPeriod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
