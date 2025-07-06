package jonatasSantos.royalLux.core.application.usecases.materialsalonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class MaterialSalonServiceDeleteUseCaseImpl implements MaterialSalonServiceDeleteUseCase {

    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final UserRepository userRepository;

    public MaterialSalonServiceDeleteUseCaseImpl(MaterialSalonServiceRepository materialSalonServiceRepository, UserRepository userRepository) {
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public MaterialSalonServiceDeleteUseCaseOutputDto execute(User user, Integer id) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var materialSalonService = this.materialSalonServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre material e serviço é inexistente"));

        this.materialSalonServiceRepository.delete(materialSalonService);

        return new MaterialSalonServiceDeleteUseCaseOutputDto(true);
    }
}
