package jonatasSantos.royalLux.core.application.usecases.client;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ClientDeleteUseCaseImpl implements ClientDeleteUseCase {

    private final ClientRepository clientRepository;
    private final CustomerServiceRepository customerServiceRepository;

    public ClientDeleteUseCaseImpl(ClientRepository clientRepository, CustomerServiceRepository customerServiceRepository) {
        this.clientRepository = clientRepository;
        this.customerServiceRepository = customerServiceRepository;
    }

    @CacheEvict(value = "clientList", allEntries = true)
    @AuditLogAnnotation
    @Override
    public ClientDeleteUseCaseOutputDto execute(User user, Integer id) {
        var client = this.clientRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Cliente inexistente"));

        var customerServices = this.customerServiceRepository.findByClientId(client.getId());

        if (!customerServices.isEmpty())
            throw new IllegalStateException("Cliente não pode ser deletado pois ainda possui atendimentos vinculados");

        this.clientRepository.delete(client);

        return new ClientDeleteUseCaseOutputDto(true);
    }
}