package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SalonServiceGetUseCaseInputDto(Integer id, String name, String description, LocalTime estimatedTime, BigDecimal value) {

    public boolean filterIsEmpty() {
        return id == null && name == null && description == null && estimatedTime == null && value == null;
    }
}
