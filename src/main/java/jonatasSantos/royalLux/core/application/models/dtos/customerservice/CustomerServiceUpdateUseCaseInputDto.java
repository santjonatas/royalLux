package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;

import java.time.LocalDateTime;

public record CustomerServiceUpdateUseCaseInputDto(CustomerServiceStatus status, LocalDateTime finishingTime, String details) {
}
