package jonatasSantos.royalLux.core.application.models.dtos.payment;

import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;

public record PaymentUpdateUseCaseInputDto(PaymentStatus status, String description, String payerName) {
}
