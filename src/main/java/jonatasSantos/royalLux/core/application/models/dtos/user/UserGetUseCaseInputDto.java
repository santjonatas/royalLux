package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.enums.UserRole;

public record UserGetUseCaseInputDto (Integer id, String username, UserRole role, Boolean active){}