package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class RoleDeleteUseCaseImpl implements RoleDeleteUseCase {

    private final RoleRepository roleRepository;

    public RoleDeleteUseCaseImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDeleteUseCaseOutputDto execute(Integer id) {
        var role = this.roleRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Função inexistente"));

        this.roleRepository.delete(role);

        return new RoleDeleteUseCaseOutputDto(true);
    }
}
