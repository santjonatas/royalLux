package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SalonServiceGetUseCaseOutputDto (Integer id, String name, String description, LocalTime estimatedTime, BigDecimal value, LocalDateTime createdAt, LocalDateTime updatedAt) implements Serializable {
    private static final long serialVersionUID = 1L;
}
