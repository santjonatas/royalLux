package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record SalonServiceCustomerServiceCreateUseCaseInputDto(Integer customerServiceId, Integer salonServiceId, Integer employeeId, LocalTime startTime, boolean completed) {
}
