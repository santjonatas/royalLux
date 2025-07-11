package jonatasSantos.royalLux.core.application.usecases.role;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.RoleRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.role.RoleCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Role;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class RoleCreateUseCaseImpl implements RoleCreateUseCase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleCreateUseCaseImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @AuditLogAnnotation
    @Override
    public RoleCreateUseCaseOutputDto execute(User user, RoleCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        if(this.roleRepository.existsByName(input.name()))
            throw new ConflictException("Função já existente com este nome");

        Role role = new Role(input.name(), input.detail());
        this.roleRepository.save(role);

        return new RoleCreateUseCaseOutputDto(role.getId());
    }
}
