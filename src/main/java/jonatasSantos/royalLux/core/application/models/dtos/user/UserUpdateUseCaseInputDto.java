package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.enums.UserRole;

public record UserUpdateUseCaseInputDto(String username, UserRole role, boolean active) { }
