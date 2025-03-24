package jonatasSantos.royalLux.core.application.models.dtos.client;

import java.time.LocalDateTime;

public record ClientGetUseCaseOutputDto(Integer id, Integer userId, LocalDateTime createdAt, LocalDateTime updatedAt){}