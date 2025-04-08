package jonatasSantos.royalLux.core.application.models.dtos.material;

import java.math.BigDecimal;

public record MaterialGetUseCaseInputDto(Integer id, String name, String description, BigDecimal value, Integer quantity) {
}
