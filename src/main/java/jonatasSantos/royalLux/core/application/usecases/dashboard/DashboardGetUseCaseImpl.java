package jonatasSantos.royalLux.core.application.usecases.dashboard;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.dashboard.DashboardGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.DashboardPeriod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardGetUseCaseImpl implements DashboardGetUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final CustomerServiceRepository customerServiceRepository;

    public DashboardGetUseCaseImpl(UserRepository userRepository, PaymentRepository paymentRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, CustomerServiceRepository customerServiceRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.customerServiceRepository = customerServiceRepository;
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
                var totalPayments = payments.stream().count();

                BigDecimal profit = BigDecimal.ZERO;
                for (Payment payment : payments) {
                    var customerService = this.customerServiceRepository.findById(payment.getCustomerService().getId().toString());
                    if (customerService.isPresent()) {
                        profit = profit.add(customerService.get().getTotalValue());
                    }
                }

                var salonServicesCustomerServices = this.salonServiceCustomerServiceRepository.findAllByCreatedAtBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
                var totalServices = salonServicesCustomerServices.stream().count();

                Map<SalonService, Long> quantitySalonServices = salonServicesCustomerServices.stream()
                        .collect(Collectors.groupingBy(
                                SalonServiceCustomerService::getSalonService,
                                Collectors.counting()
                        ));

                var mostCommonSalonService = quantitySalonServices.entrySet().stream()
                        .max(Map.Entry.<SalonService, Long>comparingByValue())
                        .map(Map.Entry::getKey);

                List<SalonServiceCustomerService> mostCommonSalonServiceList = mostCommonSalonService
                        .map(salonService ->
                                salonServicesCustomerServices.stream()
                                        .filter(item -> item.getSalonService().equals(salonService))
                                        .collect(Collectors.toList())
                        )
                        .orElse(Collections.emptyList());
                var totalMostCommonSalonService = mostCommonSalonServiceList.stream().count();

                System.out.println("");
            }

        }

        return List.of();
    }
}
