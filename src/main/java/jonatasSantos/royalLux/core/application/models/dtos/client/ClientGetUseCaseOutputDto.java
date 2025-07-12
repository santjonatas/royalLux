package jonatasSantos.royalLux.core.application.models.dtos.client;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ClientGetUseCaseOutputDto(Integer id, Integer userId, LocalDateTime createdAt, LocalDateTime updatedAt) implements Serializable{
    private static final long serialVersionUID = 1L;
}