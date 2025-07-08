package jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SchedulerConfigGetUseCaseOutputDto(Integer id, String name, String description, LocalDate date, Boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
