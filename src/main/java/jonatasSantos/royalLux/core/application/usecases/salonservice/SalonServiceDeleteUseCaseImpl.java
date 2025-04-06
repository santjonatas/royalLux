package jonatasSantos.royalLux.core.application.usecases.salonservice;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceDeleteUseCaseOutputDto;
import org.springframework.stereotype.Service;


@Service
public class SalonServiceDeleteUseCaseImpl implements SalonServiceDeleteUseCase {

    private final SalonServiceRepository salonServiceRepository;

    public SalonServiceDeleteUseCaseImpl(SalonServiceRepository salonServiceRepository) {
        this.salonServiceRepository = salonServiceRepository;
    }

    @Override
    public SalonServiceDeleteUseCaseOutputDto execute(Integer id) {
        var salonService = this.salonServiceRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Serviço inexistente"));

        this.salonServiceRepository.delete(salonService);

        return new SalonServiceDeleteUseCaseOutputDto(true);
    }
}
