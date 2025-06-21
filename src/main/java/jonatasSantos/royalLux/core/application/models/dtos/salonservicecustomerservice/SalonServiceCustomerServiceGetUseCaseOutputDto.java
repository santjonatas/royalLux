package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SalonServiceCustomerServiceGetUseCaseOutputDto(Integer id, Integer customerServiceId, Integer salonServiceId, Integer employeeId, LocalDate date, LocalTime startTime, LocalTime estimatedFinishingTime, boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
