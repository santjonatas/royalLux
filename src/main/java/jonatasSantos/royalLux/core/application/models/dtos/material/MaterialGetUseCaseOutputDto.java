package jonatasSantos.royalLux.core.application.models.dtos.material;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaterialGetUseCaseOutputDto(Integer id, String name, String description, BigDecimal value, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
