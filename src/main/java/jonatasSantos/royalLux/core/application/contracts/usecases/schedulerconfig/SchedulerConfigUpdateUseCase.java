package jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig;

import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface SchedulerConfigUpdateUseCase {
    public SchedulerConfigUpdateUseCaseOutputDto execute(User user, Integer schedulerConfigId, SchedulerConfigUpdateUseCaseInputDto input);
}
