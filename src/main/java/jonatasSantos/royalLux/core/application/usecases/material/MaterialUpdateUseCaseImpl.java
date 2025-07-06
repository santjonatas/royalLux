package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MaterialUpdateUseCaseImpl implements MaterialUpdateUseCase {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialUpdateUseCaseImpl(MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public MaterialUpdateUseCaseOutputDto execute(User user, Integer materialId, MaterialUpdateUseCaseInputDto input) {
        var materialToBeUpdated = this.materialRepository.findById(String.valueOf(materialId))
                .orElseThrow(() -> new EntityNotFoundException("Material é inexistente"));

        if(this.materialRepository.existsByNameAndIdNot(input.name(), materialToBeUpdated.getId()))
            throw new ConflictException("Material já existente com este nome");

        ArrayList<String> warningList = new ArrayList<>();

        materialToBeUpdated.setName(input.name());
        materialToBeUpdated.setDescription(input.description());
        materialToBeUpdated.setValue(input.value());
        materialToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.materialRepository.save(materialToBeUpdated);

        return new MaterialUpdateUseCaseOutputDto(true, warningList);
    }
}
