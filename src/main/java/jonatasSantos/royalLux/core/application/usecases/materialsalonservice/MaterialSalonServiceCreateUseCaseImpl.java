package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class MaterialSalonServiceCreateUseCaseImpl implements MaterialSalonServiceCreateUseCase {

    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final MaterialRepository materialRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final UserRepository userRepository;

    public MaterialSalonServiceCreateUseCaseImpl(MaterialSalonServiceRepository materialSalonServiceRepository, MaterialRepository materialRepository, SalonServiceRepository salonServiceRepository, UserRepository userRepository) {
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.materialRepository = materialRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MaterialSalonServiceCreateUseCaseOutputDto execute(User user, MaterialSalonServiceCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var material = this.materialRepository.findById(String.valueOf(input.materialId()))
                .orElseThrow(() -> new EntityNotFoundException("Material é inexistente"));

        var salonService = this.salonServiceRepository.findById(String.valueOf(input.salonServiceId()))
                .orElseThrow(() -> new EntityNotFoundException("Serviço é inexistente"));

        if(this.materialSalonServiceRepository.existsByMaterialIdAndSalonServiceId(material.getId(), salonService.getId()))
            throw new ConflictException("Material já vinculado a esse serviço");

        MaterialSalonService materialSalonService = new MaterialSalonService(
                salonService,
                material,
                input.quantityMaterial()
        );

        this.materialSalonServiceRepository.save(materialSalonService);

        return new MaterialSalonServiceCreateUseCaseOutputDto(materialSalonService.getId());
    }
}
