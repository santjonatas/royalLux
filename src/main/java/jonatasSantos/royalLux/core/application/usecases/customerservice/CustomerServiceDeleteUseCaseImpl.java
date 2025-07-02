package jonatasSantos.royalLux.core.application.usecases.customerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante: Adicione este import

@Service
public class CustomerServiceDeleteUseCaseImpl implements CustomerServiceDeleteUseCase {

    private final CustomerServiceRepository customerServiceRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final PaymentRepository paymentRepository;

    public CustomerServiceDeleteUseCaseImpl(CustomerServiceRepository customerServiceRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, PaymentRepository paymentRepository) {
        this.customerServiceRepository = customerServiceRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public CustomerServiceDeleteUseCaseOutputDto execute(Integer id) {
        var customerService = this.customerServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Atendimento inexistente"));

        if (this.salonServiceCustomerServiceRepository.existsByCustomerServiceId(customerService.getId()))
            this.salonServiceCustomerServiceRepository.deleteByCustomerServiceId(customerService.getId());

        if (this.paymentRepository.existsByCustomerServiceId(customerService.getId()))
            this.paymentRepository.deleteByCustomerServiceId(customerService.getId());

        this.customerServiceRepository.delete(customerService);

        return new CustomerServiceDeleteUseCaseOutputDto(true);
    }
}