package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class SalonServiceCustomerServiceCreateUseCaseImpl implements SalonServiceCustomerServiceCreateUseCase {

    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public SalonServiceCustomerServiceCreateUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, CustomerServiceRepository customerServiceRepository, SalonServiceRepository salonServiceRepository, EmployeeRepository employeeRepository, MaterialSalonServiceRepository materialSalonServiceRepository, MaterialRepository materialRepository, UserRepository userRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.employeeRepository = employeeRepository;
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.materialRepository = materialRepository;
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

        var materialsSalonService = this.materialSalonServiceRepository.findBySalonServiceId(salonService.getId());

        if(!materialsSalonService.isEmpty())
            materialsSalonService.forEach((materialSalonService) -> {
                var material = this.materialRepository.findById(materialSalonService.getMaterial().getId().toString());

                if(material.get().getAvailableQuantity() < materialSalonService.getQuantityMaterial())
                    throw new IllegalArgumentException("A quantidade de "
                    + material.get().getName() + " necessária para realizar este serviço é " + materialSalonService.getQuantityMaterial()
                    + ", mas seu estoque só possui " + material.get().getAvailableQuantity());
            });

        if(CustomerServiceStatus.FINISHED_STATUSES.contains(customerService.getStatus()))
            throw new IllegalArgumentException("Não é possível criar um novo serviço para este atendimento pois ele já foi finalizado");

        if(input.startTime().isBefore(customerService.getStartTime().toLocalTime()))
            throw new IllegalArgumentException("Não é possível criar um novo serviço para este atendimento antes do seu horário de início");

        var salonServicesCustomerServicesByEmployeeIdAndDate = this.salonServiceCustomerServiceRepository.findByEmployeeIdAndDate(employee.getId(), customerService.getStartTime().toLocalDate());

        if(!salonServicesCustomerServicesByEmployeeIdAndDate.isEmpty())
            salonServicesCustomerServicesByEmployeeIdAndDate.forEach(service -> {
                if((input.startTime().equals(service.getStartTime()) || input.startTime().isAfter(service.getStartTime())) && input.startTime().isBefore(service.getEstimatedFinishingTime()))
                    throw new ConflictException("O funcionário " + employee.getUser().getUsername() +
                            " já está vinculado a um serviço do atendimento " + service.getCustomerService().getId() + " neste horário");
            });

        customerService.incrementTotalValue(salonService.getValue());

        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService(
                customerService,
                salonService,
                employee,
                customerService.getStartTime().toLocalDate(),
                input.startTime(),
                input.completed()
        );

        salonServiceCustomerService.incrementEstimatedFinishingTime(salonService.getEstimatedTime());

        if(!salonServicesCustomerServicesByEmployeeIdAndDate.isEmpty())
            salonServicesCustomerServicesByEmployeeIdAndDate.forEach(service -> {
                if(salonServiceCustomerService.getEstimatedFinishingTime().isAfter(service.getStartTime()) && !input.startTime().isAfter(service.getEstimatedFinishingTime()))
                    throw new ConflictException("O funcionário " + employee.getUser().getUsername() +
                            " já está vinculado a um serviço do atendimento " + service.getCustomerService().getId() + " neste horário");
            });

        this.salonServiceCustomerServiceRepository.save(salonServiceCustomerService);

        var salonServicesCustomerServicesByCustomerService = this.salonServiceCustomerServiceRepository.findByCustomerServiceId(customerService.getId());

        var maxTime = salonServicesCustomerServicesByCustomerService.stream()
                .map(SalonServiceCustomerService::getEstimatedFinishingTime)
                .max(Comparator.naturalOrder())
                .orElse(null);

        customerService.setEstimatedFinishingTime(maxTime.atDate(customerService.getStartTime().toLocalDate()));

        this.customerServiceRepository.save(customerService);

        return new SalonServiceCustomerServiceCreateUseCaseOutputDto(salonServiceCustomerService.getId());
    }
}
