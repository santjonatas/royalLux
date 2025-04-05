package jonatasSantos.royalLux.core.application.usecases.service;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.SalonServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.service.SalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.service.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.service.SalonServiceCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.SalonService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class SalonSalonServiceCreateUseCaseImpl implements SalonServiceCreateUseCase {

    private final SalonServiceRepository salonServiceRepository;
    private final UserRepository userRepository;

    public SalonSalonServiceCreateUseCaseImpl(SalonServiceRepository salonServiceRepository, UserRepository userRepository) {
        this.salonServiceRepository = salonServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SalonServiceCreateUseCaseOutputDto execute(User user, SalonServiceCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        if(this.salonServiceRepository.existsByName(input.name()))
            throw new ConflictException("Serviço já existente com este nome");

        SalonService salonService = new SalonService(
                input.name(),
                input.description(),
                input.estimatedTime(),
                input.value()
        );

        this.salonServiceRepository.save(salonService);

        return new SalonServiceCreateUseCaseOutputDto(salonService.getId());
    }
}
