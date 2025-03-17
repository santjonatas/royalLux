package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.valueobjects.UserRole;

import java.time.LocalDateTime;

public record UserGetUseCaseOutputDto (Integer id, String username, UserRole role, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt){}
