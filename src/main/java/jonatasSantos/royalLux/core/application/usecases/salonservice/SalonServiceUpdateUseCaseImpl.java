package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class SalonServiceUpdateUseCaseImpl implements SalonServiceUpdateUseCase {

    private final SalonServiceRepository salonServiceRepository;
    private final UserRepository userRepository;

    public SalonServiceUpdateUseCaseImpl(SalonServiceRepository salonServiceRepository, UserRepository userRepository) {
        this.salonServiceRepository = salonServiceRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public SalonServiceUpdateUseCaseOutputDto execute(User user, Integer salonServiceId, SalonServiceUpdateUseCaseInputDto input) {
        var salonServiceToBeUpdated = this.salonServiceRepository.findById(String.valueOf(salonServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Serviço é inexistente"));

        if(this.salonServiceRepository.existsByNameAndIdNot(input.name(), salonServiceToBeUpdated.getId()))
            throw new ConflictException("Serviço já existente com este nome");

        ArrayList<String> warningList = new ArrayList<>();

        salonServiceToBeUpdated.setName(input.name());
        salonServiceToBeUpdated.setDescription(input.description());
        salonServiceToBeUpdated.setEstimatedTime(input.estimatedTime());
        salonServiceToBeUpdated.setValue(input.value());
        salonServiceToBeUpdated.setUpdatedAt(LocalDateTime.now());
        this.salonServiceRepository.save(salonServiceToBeUpdated);

        return new SalonServiceUpdateUseCaseOutputDto(true, warningList);
    }
}
