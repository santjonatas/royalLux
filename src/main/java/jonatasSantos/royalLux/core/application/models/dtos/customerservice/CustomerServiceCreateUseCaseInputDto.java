package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
import java.time.LocalDateTime;

public record CustomerServiceCreateUseCaseInputDto(Integer clientId, CustomerServiceStatus status, LocalDateTime startTime, String details) {
}
