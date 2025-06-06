package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SalonServiceUpdateUseCaseInputDto(String name, String description, @DateTimeFormat(pattern = "HH:mm:ss") LocalTime estimatedTime, BigDecimal value) {
}
