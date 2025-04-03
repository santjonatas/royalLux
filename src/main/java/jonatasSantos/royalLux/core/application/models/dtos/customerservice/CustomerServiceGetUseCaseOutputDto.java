package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerServiceGetUseCaseOutputDto(Integer id, Integer createdByUserId, Integer clientId, String status, LocalDateTime startTime, LocalDateTime estimatedFinishingTime, LocalDateTime finishingTime, BigDecimal totalValue, String details, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
