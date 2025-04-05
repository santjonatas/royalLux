package jonatasSantos.royalLux.core.application.models.dtos.service;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SalonServiceCreateUseCaseInputDto(String name, String description, @DateTimeFormat(pattern = "HH:mm:ss") LocalTime estimatedTime, BigDecimal value) {
}
