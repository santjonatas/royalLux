package jonatasSantos.royalLux.infra.services;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import jonatasSantos.royalLux.core.application.contracts.services.SensitiveDataMasker;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensitiveDataMaskerImpl implements SensitiveDataMasker {

    public String maskSensitiveFields(String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonStr);

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
                    if (key.toLowerCase().contains("password")) {
                        JsonNode valueNode = obj.get(key);
                        if (valueNode != null && valueNode.isTextual()) {
                            String original = valueNode.asText();
                            obj.put(key, "*".repeat(original.length()));
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

    public String formatStackTrace(Throwable throwable) {
        if (throwable == null) return "";

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

}
