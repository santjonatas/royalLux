package jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig;

import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import java.util.List;

public interface SchedulerConfigGetUseCase {
    List<SchedulerConfigGetUseCaseOutputDto> execute(User user, SchedulerConfigGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
