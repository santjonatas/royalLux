package jonatasSantos.royalLux.core.domain.enums;

public enum CodeDeliveryChannel {
    EMAIL("E-mail"),
    WHATSAPP("WhatsApp");

    private final String displayName;

    CodeDeliveryChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
