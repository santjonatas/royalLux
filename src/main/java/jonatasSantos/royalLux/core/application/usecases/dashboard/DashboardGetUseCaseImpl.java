package jonatasSantos.royalLux.core.application.usecases.dashboard;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.dashboard.DashboardGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.DashboardPeriod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardGetUseCaseImpl implements DashboardGetUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public DashboardGetUseCaseImpl(UserRepository userRepository, PaymentRepository paymentRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @Override
    public List<DashboardGetUseCaseOutputDto> execute(User user, DashboardGetUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            if(input.period().equals(DashboardPeriod.DIARIO)){
                LocalDate date = LocalDate.now();

                var payments = this.paymentRepository.findAllByCreatedAtBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
                payments = payments.stream().filter(payment -> payment.getStatus().equals(PaymentStatus.PAGO)).toList();

                var salonServicesCustomerServices = this.salonServiceCustomerServiceRepository.findAllByCreatedAtBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
                Map<SalonService, Long> quantityServices = salonServicesCustomerServices.stream()
                        .collect(Collectors.groupingBy(
                                SalonServiceCustomerService::getSalonService,
                                Collectors.counting()
                        ));


                System.out.println("");
            }

        }

        return List.of();
    }
}
