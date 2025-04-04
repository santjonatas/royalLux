package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import java.time.LocalDateTime;

public record CustomerServiceUpdateUseCaseInputDto(String status, LocalDateTime finishingTime, String details) {
}
