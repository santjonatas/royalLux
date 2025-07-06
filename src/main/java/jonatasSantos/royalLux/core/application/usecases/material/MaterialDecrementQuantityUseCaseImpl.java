package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialDecrementQuantityUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDecrementQuantityUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MaterialDecrementQuantityUseCaseImpl implements MaterialDecrementQuantityUseCase {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialDecrementQuantityUseCaseImpl(MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public MaterialDecrementQuantityUseCaseOutputDto execute(User user, Integer materialId, MaterialDecrementQuantityUseCaseInputDto input) {
        var materialToBeUpdated = this.materialRepository.findById(String.valueOf(materialId))
                .orElseThrow(() -> new EntityNotFoundException("Material é inexistente"));

        materialToBeUpdated.decrementQuantity(input.quantity());
        materialToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.materialRepository.save(materialToBeUpdated);

        return new MaterialDecrementQuantityUseCaseOutputDto(materialToBeUpdated.getAvailableQuantity());
    }
}
