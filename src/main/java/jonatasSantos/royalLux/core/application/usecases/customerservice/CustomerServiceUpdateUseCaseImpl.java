package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CustomerServiceUpdateUseCaseImpl implements CustomerServiceUpdateUseCase {

    private final CustomerServiceRepository customerServiceRepository;
    private final UserRepository userRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public CustomerServiceUpdateUseCaseImpl(CustomerServiceRepository customerServiceRepository, UserRepository userRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
        this.userRepository = userRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public CustomerServiceUpdateUseCaseOutputDto execute(User user, Integer customerServiceId, CustomerServiceUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var customerServiceToBeUpdated = this.customerServiceRepository.findById(String.valueOf(customerServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Atendimento é inexistente"));

        var salonServicesCustomerService = this.salonServiceCustomerServiceRepository.findBySalonServiceId(customerServiceId);

        salonServicesCustomerService.forEach(salonServiceCustomerService -> {
            if(CustomerServiceStatus.FINISHED_STATUS.contains(input.status()) && salonServiceCustomerService.isCompleted().equals(false))
                throw new IllegalStateException("Todos os serviços deste atendimento devem ter sido finalizados antes alterar o status do atendimento para " + input.status().getDescricao());
        });

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)
                && customerServiceToBeUpdated.getStatus().equals(CustomerServiceStatus.FINALIZADO)){
            throw new UnauthorizedException("Você não possui autorização para atualizar um atendimento que já foi finalizado");
        }

        customerServiceToBeUpdated.setStatus(input.status());
        customerServiceToBeUpdated.setFinishingTime(input.finishingTime());
        customerServiceToBeUpdated.setDetails(input.details());
        customerServiceToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.customerServiceRepository.save(customerServiceToBeUpdated);

        return new CustomerServiceUpdateUseCaseOutputDto(true, warningList);
    }
}
