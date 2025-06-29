package jonatasSantos.royalLux.core.application.models.dtos.payment;

import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import java.time.LocalDateTime;

public record PaymentCreateUseCaseInputDto(Integer customerServiceId, PaymentStatus status, LocalDateTime time, PaymentMethod method, String description) {
}
