package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;

@Service
public class SalonServiceCustomerServiceDeleteUseCaseImpl implements SalonServiceCustomerServiceDeleteUseCase {

    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public SalonServiceCustomerServiceDeleteUseCaseImpl(SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @Override
    public SalonServiceCustomerServiceDeleteUseCaseOutputDto execute(Integer id) {
        var salonServiceCustomerService = this.salonServiceCustomerServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre serviço, atendimento e funcionário é inexistente"));

        this.salonServiceCustomerServiceRepository.delete(salonServiceCustomerService);

        return new SalonServiceCustomerServiceDeleteUseCaseOutputDto(true);
    }
}
