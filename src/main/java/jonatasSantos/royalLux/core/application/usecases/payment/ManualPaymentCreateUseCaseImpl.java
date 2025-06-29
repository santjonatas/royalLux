package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.ManualPaymentCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import org.springframework.stereotype.Service;

import static jonatasSantos.royalLux.core.domain.enums.PaymentStatus.CASH_STATUSES;

@Service
public class ManualPaymentCreateUseCaseImpl implements ManualPaymentCreateUseCase {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CustomerServiceRepository customerServiceRepository;

    public ManualPaymentCreateUseCaseImpl(PaymentRepository paymentRepository, UserRepository userRepository, CustomerServiceRepository customerServiceRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.customerServiceRepository = customerServiceRepository;
    }

    @Override
    public ManualPaymentCreateUseCaseOutputDto execute(User user, ManualPaymentCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var customerService = this.customerServiceRepository.findById(String.valueOf(input.customerServiceId()))
                .orElseThrow(() -> new EntityNotFoundException("Atendimento inexistente"));

        if(this.paymentRepository.existsByCustomerServiceId(customerService.getId()))
            throw new ConflictException("Pagamento já foi realizado");

        if(!CASH_STATUSES.contains(input.status()))
            throw new IllegalArgumentException("Status inválido para este método de pagamento");

        Payment payment = new Payment(
                customerService,
                userLogged,
                input.status(),
                PaymentMethod.DINHEIRO,
                input.description(),
                input.payerName() != null && !input.payerName().isEmpty() ? input.payerName() : "Indefinido"
        );

        this.paymentRepository.save(payment);

        return new ManualPaymentCreateUseCaseOutputDto(payment.getId());
    }
}
