package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;

import java.time.LocalDateTime;

public record SalonServiceCustomerServiceCreateUseCaseInputDto(Integer customerServiceId, Integer salonServiceId, Integer employeeId, LocalDateTime startTime, LocalDateTime finishingTime, boolean completed) {
}
