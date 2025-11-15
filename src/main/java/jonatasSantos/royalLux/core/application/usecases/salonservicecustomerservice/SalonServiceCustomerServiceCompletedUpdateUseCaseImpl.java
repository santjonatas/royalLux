package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class SalonServiceCustomerServiceCompletedUpdateUseCaseImpl implements SalonServiceCustomerServiceCompletedUpdateUseCase {

    private final UserRepository userRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public SalonServiceCustomerServiceCompletedUpdateUseCaseImpl(UserRepository userRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.userRepository = userRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto execute(User user, Integer salonServiceCustomerServiceId, SalonServiceCustomerServiceCompletedUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var salonServiceCustomerServiceToBeUpdated = this.salonServiceCustomerServiceRepository.findById(String.valueOf(salonServiceCustomerServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre serviço e atendimento é inexistente"));

        if(user.getRole().equals(UserRole.EMPLOYEE) && input.completed().equals(false)) {
            throw new UnauthorizedException("Você não possui permissão para alterar o status deste serviço para não realizado");
        }

        salonServiceCustomerServiceToBeUpdated.setCompleted(input.completed());
        this.salonServiceCustomerServiceRepository.save(salonServiceCustomerServiceToBeUpdated);

        return new SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto(salonServiceCustomerServiceToBeUpdated.isCompleted());
    }
}
