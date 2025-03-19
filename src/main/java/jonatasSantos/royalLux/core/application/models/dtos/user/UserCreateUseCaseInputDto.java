package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.enums.UserRole;

public record UserCreateUseCaseInputDto(String username, String password, UserRole role, boolean active){}
