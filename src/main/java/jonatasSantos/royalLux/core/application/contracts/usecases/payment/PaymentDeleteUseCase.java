package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentDeleteUseCaseOutputDto;

public interface PaymentDeleteUseCase {
    public PaymentDeleteUseCaseOutputDto execute(Integer id);
}
