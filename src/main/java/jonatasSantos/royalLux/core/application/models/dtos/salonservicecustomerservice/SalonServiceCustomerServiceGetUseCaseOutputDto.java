package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;


import java.time.LocalDateTime;

public record SalonServiceCustomerServiceGetUseCaseOutputDto(Integer id, Integer customerServiceId, Integer salonServiceId, Integer employeeId, LocalDateTime startTime, LocalDateTime finishingTime, boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
