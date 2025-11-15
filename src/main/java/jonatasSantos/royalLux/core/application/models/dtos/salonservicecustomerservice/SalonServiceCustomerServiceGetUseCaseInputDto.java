package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;

import jonatasSantos.royalLux.core.domain.enums.SalonServicesCustomerServiceStatus;

public record SalonServiceCustomerServiceGetUseCaseInputDto(Integer id, Integer customerServiceId, Integer salonServiceId, Integer employeeId, SalonServicesCustomerServiceStatus status) {

}
