package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PaymentUpdateUseCase {
    public PaymentUpdateUseCaseOutputDto execute(User user, Integer paymentId, PaymentUpdateUseCaseInputDto input);
}
