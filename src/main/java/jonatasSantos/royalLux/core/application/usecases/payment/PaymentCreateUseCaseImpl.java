package jonatasSantos.royalLux.core.application.usecases.payment;

import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentCreateUseCaseImpl implements PaymentCreateUseCase {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentCreateUseCaseImpl(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentCreateUseCaseOutputDto execute(User user, PaymentCreateUseCaseInputDto input) {
        return null;
    }
}
