package jonatasSantos.royalLux.core.application.contracts.usecases.dashboard;

import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface DashboardGetUseCase {
    List<DashboardGetUseCaseOutputDto> execute(User user, DashboardGetUseCaseInputDto input);
}
