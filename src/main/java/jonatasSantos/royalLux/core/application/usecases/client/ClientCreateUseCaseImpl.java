package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Client;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ClientCreateUseCaseImpl implements ClientCreateUseCase {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientCreateUseCaseImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "clientList", allEntries = true)
    @AuditLogAnnotation
    @Override
    public ClientCreateUseCaseOutputDto execute(User user, ClientCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var existingUser = this.userRepository.findById(String.valueOf(input.userId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário é inexistente"));

        if(existingUser.getRole() != UserRole.CLIENT)
            throw new IllegalArgumentException("Usuário deve ser um cliente");

        if(userLogged.getRole().equals(UserRole.CLIENT) && existingUser.getId() != userLogged.getId())
            throw new UnauthorizedException("Você não possui autorização para criar outro cliente");

        if(this.clientRepository.existsByUserId(existingUser.getId()))
            throw new ConflictException("Cliente já vinculado a um usuário");

        Client client = new Client(existingUser);

        this.clientRepository.save(client);

        return new ClientCreateUseCaseOutputDto(client.getId());
    }
}