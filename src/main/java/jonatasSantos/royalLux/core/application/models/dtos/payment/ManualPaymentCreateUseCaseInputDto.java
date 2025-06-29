package jonatasSantos.royalLux.core.application.models.dtos.payment;

import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;

public record ManualPaymentCreateUseCaseInputDto(Integer customerServiceId, PaymentStatus status, String description, String payerName) {
}
