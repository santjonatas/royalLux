package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PaymentCreateUseCase {
    public PaymentCreateUseCaseOutputDto execute(User user, PaymentCreateUseCaseInputDto input);
}
