package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceDeleteUseCaseImpl implements CustomerServiceDeleteUseCase {

    private final CustomerServiceRepository customerServiceRepository;

    public CustomerServiceDeleteUseCaseImpl(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

    @Override
    public CustomerServiceDeleteUseCaseOutputDto execute(Integer id) {
        var customerService = this.customerServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Atendimento inexistente"));

        this.customerServiceRepository.delete(customerService);

        return new CustomerServiceDeleteUseCaseOutputDto(true);
    }
}
