package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class MaterialDeleteUseCaseImpl implements MaterialDeleteUseCase {

    private final MaterialRepository materialRepository;

    public MaterialDeleteUseCaseImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public MaterialDeleteUseCaseOutputDto execute(Integer id) {
        var material = this.materialRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Material inexistente"));

        this.materialRepository.delete(material);

        return new MaterialDeleteUseCaseOutputDto(true);
    }
}
