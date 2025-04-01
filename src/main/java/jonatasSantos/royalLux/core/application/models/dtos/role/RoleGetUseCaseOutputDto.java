package jonatasSantos.royalLux.core.application.models.dtos.role;

import java.time.LocalDateTime;

public record RoleGetUseCaseOutputDto(Integer id, String name, String detail, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
