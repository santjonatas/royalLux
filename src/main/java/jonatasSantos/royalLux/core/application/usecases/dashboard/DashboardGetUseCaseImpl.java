package jonatasSantos.royalLux.core.application.usecases.dashboard;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.dashboard.DashboardGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.DashboardPeriod;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardGetUseCaseImpl implements DashboardGetUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public DashboardGetUseCaseImpl(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<DashboardGetUseCaseOutputDto> execute(User user, DashboardGetUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            LocalDate date = LocalDate.now();

            if(input.period().equals(DashboardPeriod.DIARIO)){
                var payments = this.paymentRepository.findAllByCreatedAtBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());

            }

        }

        return List.of();
    }
}
