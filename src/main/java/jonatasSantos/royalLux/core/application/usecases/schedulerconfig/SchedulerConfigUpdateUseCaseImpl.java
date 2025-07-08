package jonatasSantos.royalLux.core.application.usecases.schedulerconfig;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.SchedulerConfigRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig.SchedulerConfigUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SchedulerConfigUpdateUseCaseImpl implements SchedulerConfigUpdateUseCase {

    private final SchedulerConfigRepository schedulerConfigRepository;

    public SchedulerConfigUpdateUseCaseImpl(SchedulerConfigRepository schedulerConfigRepository) {
        this.schedulerConfigRepository = schedulerConfigRepository;
    }

    @AuditLogAnnotation
    @Override
    public SchedulerConfigUpdateUseCaseOutputDto execute(User user, Integer schedulerConfigId, SchedulerConfigUpdateUseCaseInputDto input) {
        var schedulerConfigToBeUpdated = this.schedulerConfigRepository.findById(String.valueOf(schedulerConfigId))
                .orElseThrow(() -> new EntityNotFoundException("Agendamento é inexistente"));

        schedulerConfigToBeUpdated.setDate(input.date());
        schedulerConfigToBeUpdated.setEnabled(input.enabled());
        schedulerConfigToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.schedulerConfigRepository.save(schedulerConfigToBeUpdated);

        return new SchedulerConfigUpdateUseCaseOutputDto(true);
    }
}
