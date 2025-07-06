package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;


@Service
public class SalonServiceDeleteUseCaseImpl implements SalonServiceDeleteUseCase {

    private final SalonServiceRepository salonServiceRepository;
    private final MaterialSalonServiceRepository materialSalonServiceRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;

    public SalonServiceDeleteUseCaseImpl(SalonServiceRepository salonServiceRepository, MaterialSalonServiceRepository materialSalonServiceRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository) {
        this.salonServiceRepository = salonServiceRepository;
        this.materialSalonServiceRepository = materialSalonServiceRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public SalonServiceDeleteUseCaseOutputDto execute(User user, Integer id) {
        var salonService = this.salonServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Serviço inexistente"));

        var materialsSalonServices = this.materialSalonServiceRepository.findBySalonServiceId(salonService.getId());

        if (!materialsSalonServices.isEmpty())
            throw new IllegalStateException("Serviço não pode ser deletado pois ainda possui materiais vinculados");

        var salonServicesCustomerServices = this.salonServiceCustomerServiceRepository.findBySalonServiceId(salonService.getId());

        if (!salonServicesCustomerServices.isEmpty())
            throw new IllegalStateException("Serviço não pode ser deletado pois ainda possui serviços de atendimentos vinculados");

        this.salonServiceRepository.delete(salonService);

        return new SalonServiceDeleteUseCaseOutputDto(true);
    }
}
