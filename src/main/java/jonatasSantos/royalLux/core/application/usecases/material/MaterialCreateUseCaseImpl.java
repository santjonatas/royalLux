package jonatasSantos.royalLux.core.application.usecases.material;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class MaterialCreateUseCaseImpl implements MaterialCreateUseCase {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MaterialCreateUseCaseImpl(MaterialRepository materialRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MaterialCreateUseCaseOutputDto execute(User user, MaterialCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        if(this.materialRepository.existsByName(input.name()))
            throw new ConflictException("Material já existente com este nome");

        Material material = new Material(
                input.name(),
                input.description(),
                input.value(),
                input.availableQuantity()
        );

        this.materialRepository.save(material);

        return new MaterialCreateUseCaseOutputDto(material.getId());
    }
}
