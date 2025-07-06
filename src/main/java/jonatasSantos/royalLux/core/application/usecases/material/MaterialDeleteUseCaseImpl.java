package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class MaterialDeleteUseCaseImpl implements MaterialDeleteUseCase {

    private final MaterialRepository materialRepository;
    private final MaterialSalonServiceRepository materialSalonServiceRepository;

    public MaterialDeleteUseCaseImpl(MaterialRepository materialRepository, MaterialSalonServiceRepository materialSalonServiceRepository) {
        this.materialRepository = materialRepository;
        this.materialSalonServiceRepository = materialSalonServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public MaterialDeleteUseCaseOutputDto execute(User user, Integer id) {
        var material = this.materialRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Material inexistente"));

        var materialsSalonServices = this.materialSalonServiceRepository.findByMaterialId(material.getId());

        if (!materialsSalonServices.isEmpty())
            throw new IllegalStateException("Material não pode ser deletado pois ainda possui serviços vinculados");

        this.materialRepository.delete(material);

        return new MaterialDeleteUseCaseOutputDto(true);
    }
}
