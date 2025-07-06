package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface PaymentDeleteUseCase {
    public PaymentDeleteUseCaseOutputDto execute(User user, Integer id);
}
