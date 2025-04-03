package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.ClientRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceCreateUseCaseImpl implements CustomerServiceCreateUseCase {

    private final ClientRepository clientRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final UserRepository userRepository;

    public CustomerServiceCreateUseCaseImpl(ClientRepository clientRepository, CustomerServiceRepository customerServiceRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CustomerServiceCreateUseCaseOutputDto execute(User user, CustomerServiceCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var client = this.clientRepository.findById(String.valueOf(input.clientId()))
                .orElseThrow(() -> new EntityNotFoundException("Cliente é inexistente"));

        CustomerService customerService = new CustomerService(
                userLogged,
                client,
                input.status().getDescricao(),
                input.startTime(),
                null,
                null,
                BigDecimal.ZERO,
                input.details()
        );

        this.customerServiceRepository.save(customerService);

        return new CustomerServiceCreateUseCaseOutputDto(customerService.getId());
    }
}
