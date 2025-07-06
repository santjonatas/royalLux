package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialIncrementQuantityUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialIncrementQuantityUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MaterialIncrementQuantityUseCaseImpl implements MaterialIncrementQuantityUseCase {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialIncrementQuantityUseCaseImpl(MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public MaterialIncrementQuantityUseCaseOutputDto execute(User user, Integer materialId, MaterialIncrementQuantityUseCaseInputDto input) {
        var materialToBeUpdated = this.materialRepository.findById(String.valueOf(materialId))
                .orElseThrow(() -> new EntityNotFoundException("Material é inexistente"));

        materialToBeUpdated.incrementQuantity(input.quantity());
        materialToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.materialRepository.save(materialToBeUpdated);

        return new MaterialIncrementQuantityUseCaseOutputDto(materialToBeUpdated.getAvailableQuantity());
    }
}
