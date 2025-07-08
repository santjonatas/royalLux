package jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig;


public record SchedulerConfigGetUseCaseInputDto(Integer id, String name, String description, Boolean enabled, Integer year, Integer month, Integer day) {
}
