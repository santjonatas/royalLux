package jonatasSantos.royalLux.core.application.models.dtos.role;

import java.io.Serializable;
import java.time.LocalDateTime;

public record RoleGetUseCaseOutputDto(Integer id, String name, String detail, LocalDateTime createdAt, LocalDateTime updatedAt) implements Serializable {
    private static final long serialVersionUID = 1L;
}
