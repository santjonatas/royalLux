package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalonServiceCustomerServiceDeleteUseCaseImpl implements SalonServiceCustomerServiceDeleteUseCase {

    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final CustomerServiceRepository customerServiceRepository;

    public SalonServiceCustomerServiceDeleteUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, SalonServiceRepository salonServiceRepository, CustomerServiceRepository customerServiceRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.salonServiceRepository = salonServiceRepository;
        this.customerServiceRepository = customerServiceRepository;
    }

    @Override
    @Transactional
    public SalonServiceCustomerServiceDeleteUseCaseOutputDto execute(Integer id) {
        var salonServiceCustomerService = this.salonServiceCustomerServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre serviço, atendimento e funcionário é inexistente"));

        var customerService = this.customerServiceRepository.findById(String.valueOf(salonServiceCustomerService.getCustomerService().getId()))
                .orElseThrow(() -> new EntityNotFoundException("Atendimento é inexistente"));

        var salonService = this.salonServiceRepository.findById(String.valueOf(salonServiceCustomerService.getSalonService().getId()))
                .orElseThrow(() -> new EntityNotFoundException("Serviço é inexistente"));

        customerService.decrementTotalValue(salonService.getValue());

        this.salonServiceCustomerServiceRepository.delete(salonServiceCustomerService);
        this.customerServiceRepository.save(customerService);

        return new SalonServiceCustomerServiceDeleteUseCaseOutputDto(true);
    }
}
