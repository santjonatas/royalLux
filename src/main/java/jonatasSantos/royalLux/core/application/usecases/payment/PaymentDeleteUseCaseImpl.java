package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class PaymentDeleteUseCaseImpl implements PaymentDeleteUseCase {

    private final PaymentRepository paymentRepository;

    public PaymentDeleteUseCaseImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDeleteUseCaseOutputDto execute(Integer id) {
        var payment = this.paymentRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Pagamento inexistente"));

        this.paymentRepository.delete(payment);

        return new PaymentDeleteUseCaseOutputDto(true);
    }
}
