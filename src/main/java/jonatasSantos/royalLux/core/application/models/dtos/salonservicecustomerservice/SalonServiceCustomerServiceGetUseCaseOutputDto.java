package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;


import jonatasSantos.royalLux.core.domain.enums.SalonServicesCustomerServiceStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SalonServiceCustomerServiceGetUseCaseOutputDto(Integer id, Integer customerServiceId, Integer salonServiceId, Integer employeeId, LocalDate date, LocalTime startTime, LocalTime estimatedFinishingTime, SalonServicesCustomerServiceStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
