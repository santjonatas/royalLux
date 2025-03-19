package jonatasSantos.royalLux.core.application.models.dtos.user;

import jonatasSantos.royalLux.core.domain.enums.UserRole;
import java.time.LocalDateTime;

public record UserGetMeUseCaseOutputDto(Integer id, String username, UserRole role, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt){}
