package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;

public record UserGetMeUseCaseOutputDto(Integer id, String username, UserRole role, boolean active){}
