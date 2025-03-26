package jonatasSantos.royalLux.core.application.usecases.client;

import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class ClientDeleteUseCaseImpl implements ClientDeleteUseCase {

    private final ClientRepository clientRepository;

    public ClientDeleteUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDeleteUseCaseOutputDto execute(Integer id) {
        return null;
    }
}