package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import jonatasSantos.royalLux.core.domain.entities.Client;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerServiceGetUseCaseOutputDto(Integer id, Integer createdByUserId, Client client, String status, LocalDateTime startTime, LocalDateTime estimatedFinishingTime, LocalDateTime finishingTime, BigDecimal totalValue, String details, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
