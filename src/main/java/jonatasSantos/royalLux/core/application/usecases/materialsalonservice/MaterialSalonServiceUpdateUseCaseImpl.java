package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseOutputDto;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MaterialSalonServiceUpdateUseCaseImpl implements MaterialSalonServiceUpdateUseCase {

    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final UserRepository userRepository;

    public MaterialSalonServiceUpdateUseCaseImpl(MaterialSalonServiceRepository materialSalonServiceRepository, UserRepository userRepository) {
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MaterialSalonServiceUpdateUseCaseOutputDto execute(Integer materialSalonServiceId, MaterialSalonServiceUpdateUseCaseInputDto input) {
        var materialSalonServiceToBeUpdated = this.materialSalonServiceRepository.findById(String.valueOf(materialSalonServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre material e serviço é inexistente"));

        ArrayList<String> warningList = new ArrayList<>();

        materialSalonServiceToBeUpdated.setQuantityMaterial(input.quantityMaterial());
        materialSalonServiceToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.materialSalonServiceRepository.save(materialSalonServiceToBeUpdated);

        return new MaterialSalonServiceUpdateUseCaseOutputDto(true, warningList);
    }
}
