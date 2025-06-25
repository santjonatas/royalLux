package jonatasSantos.royalLux.core.application.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhatsAppMessageMapper {

    @JsonProperty("messaging_product")
    private final String messagingProduct = "whatsapp";

    @JsonProperty("to")
    private final String to;

    @JsonProperty("type")
    private final String type = "text";

    @JsonProperty("text")
    private final Text text;

    public WhatsAppMessageMapper(String to, String body) {
        this.to = to;
        this.text = new Text(body);
    }

    public static class Text {
        @JsonProperty("body")
        private final String body;

        public Text(String body) {
            this.body = body;
        }
    }
}
