package jonatasSantos.royalLux.core.application.contracts.usecases.dashboard;

import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardAdminGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardAdminGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;


public interface DashboardAdminGetUseCase {
    DashboardAdminGetUseCaseOutputDto execute(User user, DashboardAdminGetUseCaseInputDto input);
}
