package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PaymentUpdateUseCaseImpl implements PaymentUpdateUseCase {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentUpdateUseCaseImpl(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentUpdateUseCaseOutputDto execute(User user, Integer paymentId, PaymentUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var paymentToBeUpdated = this.paymentRepository.findById(String.valueOf(paymentId))
                .orElseThrow(() -> new EntityNotFoundException("Pagamento é inexistente"));

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)) {
            if (PaymentStatus.FINISHING_STATUSES.contains(paymentToBeUpdated.getStatus()))
                throw new IllegalArgumentException("Pagamento já foi finalizado");
        }

        paymentToBeUpdated.setStatus(input.status());
        paymentToBeUpdated.setDescription(input.description());
        paymentToBeUpdated.setPayerName(input.payerName());
        paymentToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.paymentRepository.save(paymentToBeUpdated);

        return new PaymentUpdateUseCaseOutputDto(true, warningList);
    }
}
