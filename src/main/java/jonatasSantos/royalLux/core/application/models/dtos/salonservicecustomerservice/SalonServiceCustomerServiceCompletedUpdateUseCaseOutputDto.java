package jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice;

import jonatasSantos.royalLux.core.domain.enums.SalonServicesCustomerServiceStatus;

import java.util.ArrayList;

public record SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto(SalonServicesCustomerServiceStatus status) {
}
