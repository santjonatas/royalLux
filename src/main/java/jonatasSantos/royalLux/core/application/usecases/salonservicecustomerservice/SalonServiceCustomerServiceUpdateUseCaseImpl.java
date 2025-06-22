package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class SalonServiceCustomerServiceUpdateUseCaseImpl implements SalonServiceCustomerServiceUpdateUseCase {

    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public SalonServiceCustomerServiceUpdateUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SalonServiceCustomerServiceUpdateUseCaseOutputDto execute(User user, Integer salonServiceCustomerServiceId, SalonServiceCustomerServiceUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var salonServiceCustomerServiceToBeUpdated = this.salonServiceCustomerServiceRepository.findById(String.valueOf(salonServiceCustomerServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre serviço e atendimento é inexistente"));

        var employee = this.employeeRepository.findById(input.employeeId().toString())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário é inexistente"));

        var salonServicesCustomerServicesByEmployeeIdAndDate = this.salonServiceCustomerServiceRepository.findByEmployeeIdAndDate(employee.getId(), salonServiceCustomerServiceToBeUpdated.getCustomerService().getStartTime().toLocalDate());

        if(!salonServicesCustomerServicesByEmployeeIdAndDate.isEmpty())
            salonServicesCustomerServicesByEmployeeIdAndDate.forEach(service -> {
                if(salonServiceCustomerServiceId != service.getId())
                    if((salonServiceCustomerServiceToBeUpdated.getStartTime().equals(service.getStartTime()) || salonServiceCustomerServiceToBeUpdated.getStartTime().isAfter(service.getStartTime())) && salonServiceCustomerServiceToBeUpdated.getStartTime().isBefore(service.getEstimatedFinishingTime()))
                        throw new ConflictException("O funcionário " + employee.getUser().getUsername() +
                                " já está vinculado a um serviço do atendimento " + service.getCustomerService().getId() + " neste horário");
            });

        if(!salonServicesCustomerServicesByEmployeeIdAndDate.isEmpty())
            salonServicesCustomerServicesByEmployeeIdAndDate.forEach(service -> {
                if(salonServiceCustomerServiceId != service.getId())
                    if(salonServiceCustomerServiceToBeUpdated.getEstimatedFinishingTime().isAfter(service.getStartTime()) && !salonServiceCustomerServiceToBeUpdated.getStartTime().isAfter(service.getEstimatedFinishingTime()))
                        throw new ConflictException("O funcionário " + employee.getUser().getUsername() +
                                " já está vinculado a um serviço do atendimento " + service.getCustomerService().getId() + " neste horário");
            });

        ArrayList<String> warningList = new ArrayList<>();

        salonServiceCustomerServiceToBeUpdated.setEmployee(employee);
        salonServiceCustomerServiceToBeUpdated.setCompleted(input.completed());
        salonServiceCustomerServiceToBeUpdated.setUpdatedAt(LocalDateTime.now());
        this.salonServiceCustomerServiceRepository.save(salonServiceCustomerServiceToBeUpdated);

        return new SalonServiceCustomerServiceUpdateUseCaseOutputDto(true, warningList);
    }
}
