package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SalonServiceGetUseCaseOutputDto(Integer id, String name, String description, LocalTime estimatedTime, BigDecimal value, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
