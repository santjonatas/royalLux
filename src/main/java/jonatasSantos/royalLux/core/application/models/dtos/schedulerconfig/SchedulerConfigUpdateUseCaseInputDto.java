package jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig;

import java.time.LocalDate;

public record SchedulerConfigUpdateUseCaseInputDto(LocalDate date, Boolean enabled) {
}
