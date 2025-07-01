package jonatasSantos.royalLux.core.application.contracts.usecases.payment;

import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface PaymentGetUseCase {
    public List<PaymentGetUseCaseOutputDto> execute(User user, PaymentGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
