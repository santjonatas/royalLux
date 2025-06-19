package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SalonServiceCustomerServiceCreateUseCaseImpl implements SalonServiceCustomerServiceCreateUseCase {

    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public SalonServiceCustomerServiceCreateUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, CustomerServiceRepository customerServiceRepository, SalonServiceRepository salonServiceRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SalonServiceCustomerServiceCreateUseCaseOutputDto execute(User user, SalonServiceCustomerServiceCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var customerService = this.customerServiceRepository.findById(String.valueOf(input.customerServiceId()))
                .orElseThrow(() -> new EntityNotFoundException("Atendimento é inexistente"));

        var salonService = this.salonServiceRepository.findById(String.valueOf(input.salonServiceId()))
                .orElseThrow(() -> new EntityNotFoundException("Serviço é inexistente"));

        var employee = this.employeeRepository.findById(String.valueOf(input.employeeId()))
                .orElseThrow(() -> new EntityNotFoundException("Funcionário é inexistente"));

        if(!CustomerServiceStatus.FINISHED_STATUSES.contains(customerService.getStatus()) && input.finishingTime() != null)
            throw new IllegalArgumentException("Não é possível atualizar o horário de finalização desse serviço com o status de atendimento " + customerService.getStatus().getDescricao());

        customerService.incrementTotalValue(salonService.getValue());

        customerService.incrementEstimatedFinishingTime(salonService.getEstimatedTime());

        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService(
                customerService,
                salonService,
                employee,
                input.startTime(),
                input.finishingTime(),
                input.completed()
        );

        this.salonServiceCustomerServiceRepository.save(salonServiceCustomerService);
        this.customerServiceRepository.save(customerService);

        return new SalonServiceCustomerServiceCreateUseCaseOutputDto(salonServiceCustomerService.getId());
    }
}
