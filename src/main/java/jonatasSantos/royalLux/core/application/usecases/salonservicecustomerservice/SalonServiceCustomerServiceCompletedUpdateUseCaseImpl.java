package jonatasSantos.royalLux.core.application.usecases.salonservicecustomerservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.MaterialSalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceCustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Material;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.SalonServicesCustomerServiceStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SalonServiceCustomerServiceCompletedUpdateUseCaseImpl implements SalonServiceCustomerServiceCompletedUpdateUseCase {

    private final UserRepository userRepository;
    private final SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository;
    private final MaterialRepository materialRepository;
    private final MaterialSalonServiceRepository materialSalonServiceRepository;

    public SalonServiceCustomerServiceCompletedUpdateUseCaseImpl(UserRepository userRepository, SalonServiceCustomerServiceRepository salonServiceCustomerServiceRepository, MaterialRepository materialRepository, MaterialSalonServiceRepository materialSalonServiceRepository) {
        this.userRepository = userRepository;
        this.salonServiceCustomerServiceRepository = salonServiceCustomerServiceRepository;
        this.materialRepository = materialRepository;
        this.materialSalonServiceRepository = materialSalonServiceRepository;
    }

    @AuditLogAnnotation
    @Override
    public SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto execute(User user, Integer salonServiceCustomerServiceId, SalonServiceCustomerServiceCompletedUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var salonServiceCustomerServiceToBeUpdated = this.salonServiceCustomerServiceRepository.findById(String.valueOf(salonServiceCustomerServiceId))
                .orElseThrow(() -> new EntityNotFoundException("Vínculo entre serviço e atendimento é inexistente"));

        if(!SalonServicesCustomerServiceStatus.FINISHED_STATUS.contains(input.status()))
            throw new IllegalStateException("O status só pode ser atualizado para um dos status finalizados");

        if(SalonServicesCustomerServiceStatus.FINISHED_STATUS.contains(salonServiceCustomerServiceToBeUpdated.getStatus()))
            throw new IllegalStateException("O status não pode ser alterado, uma vez que o serviço já foi finalizado");

        salonServiceCustomerServiceToBeUpdated.setStatus(input.status());
        this.salonServiceCustomerServiceRepository.save(salonServiceCustomerServiceToBeUpdated);

        var materialsSalonService = this.materialSalonServiceRepository.findBySalonServiceId(salonServiceCustomerServiceToBeUpdated.getSalonService().getId());

        Map<Material, Integer> materialsAvailableQuantityMap = new HashMap<>();
        Map<Material, Integer> materialsReservedQuantityToBeDecrementedMap = new HashMap<>();

        materialsSalonService.forEach(materialSalonService -> {
            var material = this.materialRepository.findById(materialSalonService.getMaterial().getId().toString());

            materialsAvailableQuantityMap.put(material.get(), material.get().getAvailableQuantity());
        });

        materialsAvailableQuantityMap.forEach((material, availableQuantity) -> {
            var materialFound = this.materialRepository.findById(material.getId().toString());

            var materialSalonServiceFound = this.materialSalonServiceRepository.findBySalonServiceIdAndMaterialId(salonServiceCustomerServiceToBeUpdated.getSalonService().getId(), material.getId());
            materialsReservedQuantityToBeDecrementedMap.put(material, materialSalonServiceFound.getQuantityMaterial());
        });

        materialsReservedQuantityToBeDecrementedMap.forEach((material, reservedQuantityToBeDecremented) -> {
            material.decrementReservedQuantity(reservedQuantityToBeDecremented);

            if(input.status().equals(SalonServicesCustomerServiceStatus.REALIZADO))
                material.decrementAvailableQuantity(reservedQuantityToBeDecremented);

            this.materialRepository.save(material);
        });

        return new SalonServiceCustomerServiceCompletedUpdateUseCaseOutputDto(salonServiceCustomerServiceToBeUpdated.getStatus());
    }
}
