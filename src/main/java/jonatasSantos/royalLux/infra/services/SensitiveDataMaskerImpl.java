package jonatasSantos.royalLux.infra.services;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import jonatasSantos.royalLux.core.application.contracts.services.SensitiveDataMasker;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SensitiveDataMaskerImpl implements SensitiveDataMasker {

    public String maskSensitiveFields(String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonStr);

            List<String> sensitiveKeys = Arrays.asList("password", "code");
            List<ObjectNode> objectsToMask = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    if (node.isObject()) {
                        objectsToMask.add((ObjectNode) node);
                    }
                }
            } else if (root.isObject()) {
                objectsToMask.add((ObjectNode) root);
            }

            for (ObjectNode obj : objectsToMask) {
                obj.fieldNames().forEachRemaining(key -> {
                    for (String sensitive : sensitiveKeys) {
                        if (key.toLowerCase().contains(sensitive)) {
                            JsonNode valueNode = obj.get(key);
                            if (valueNode != null && valueNode.isTextual()) {
                                String original = valueNode.asText();
                                obj.put(key, "*".repeat(original.length()));
                            }
                            break;
                        }
                    }
                });
            }

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
