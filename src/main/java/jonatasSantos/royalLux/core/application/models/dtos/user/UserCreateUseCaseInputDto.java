package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;

public record UserCreateUseCaseInputDto(String username, String password, UserRole role, boolean active){}
