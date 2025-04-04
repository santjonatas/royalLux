package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerServiceGetUseCaseInputDto(Integer id,
                                                Integer createdByUserId,
                                                Integer clientId,
                                                String status,
                                                Integer startTimeYear,
                                                Integer startTimeMonth,
                                                Integer startTimeDay,
                                                Integer startTimeHour,
                                                Integer startTimeMinute,
                                                Integer estimatedFinishingTimeYear,
                                                Integer estimatedFinishingTimeMonth,
                                                Integer estimatedFinishingTimeDay,
                                                Integer estimatedFinishingTimeHour,
                                                Integer estimatedFinishingTimeMinute,
                                                Integer finishingTimeYear,
                                                Integer finishingTimeMonth,
                                                Integer finishingTimeDay,
                                                Integer finishingTimeHour,
                                                Integer finishingTimeMinute,
                                                BigDecimal totalValue,
                                                String details) {
}
