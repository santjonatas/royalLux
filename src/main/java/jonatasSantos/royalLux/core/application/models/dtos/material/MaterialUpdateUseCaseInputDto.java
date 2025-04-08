package jonatasSantos.royalLux.core.application.models.dtos.material;

import java.math.BigDecimal;

public record MaterialUpdateUseCaseInputDto(String name, String description, BigDecimal value, Integer quantity) {
}
