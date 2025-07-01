package jonatasSantos.royalLux.core.application.models.dtos.payment;

import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentGetUseCaseInputDto(Integer id,
                                        Integer customerServiceId,
                                        Integer createdByUserId,
                                        PaymentStatus status,
                                        PaymentMethod method,
                                        String description,
                                        String transactionId,
                                        String paymentToken,
                                        String paymentUrl,
                                        String payerName) {
}
