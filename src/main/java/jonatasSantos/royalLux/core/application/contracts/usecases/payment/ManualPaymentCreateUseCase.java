package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

public interface ManualPaymentCreateUseCase {
    public ManualPaymentCreateUseCaseOutputDto execute(User user, ManualPaymentCreateUseCaseInputDto input);
}
