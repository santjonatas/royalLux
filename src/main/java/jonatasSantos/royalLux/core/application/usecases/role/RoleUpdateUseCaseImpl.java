package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleUpdateUseCaseOutputDto;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RoleUpdateUseCaseImpl implements RoleUpdateUseCase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleUpdateUseCaseImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RoleUpdateUseCaseOutputDto execute(Integer roleId, RoleUpdateUseCaseInputDto input) {
        var roleToBeUpdated = this.roleRepository.findById(String.valueOf(roleId))
                .orElseThrow(() -> new EntityNotFoundException("Função é inexistente"));

        if(this.roleRepository.existsByNameAndIdNot(input.name(), roleToBeUpdated.getId()))
            throw new ConflictException("Função já existente com este nome");

        ArrayList<String> warningList = new ArrayList<>();

        roleToBeUpdated.setName(input.name());
        roleToBeUpdated.setDetail(input.detail());
        roleToBeUpdated.setUpdatedAt(LocalDateTime.now());
        this.roleRepository.save(roleToBeUpdated);

        return new RoleUpdateUseCaseOutputDto(true, warningList);
    }
}
