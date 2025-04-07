package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;

public record SalonServiceCustomerServiceGetUseCaseInputDto(Integer id, Integer customerServiceId, Integer salonServiceId, Integer employeeId, Boolean completed) {

}
