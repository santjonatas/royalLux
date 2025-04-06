package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SalonServiceGetUseCaseInputDto(Integer id, String name, String description, LocalTime estimatedTime, BigDecimal value) {
}
