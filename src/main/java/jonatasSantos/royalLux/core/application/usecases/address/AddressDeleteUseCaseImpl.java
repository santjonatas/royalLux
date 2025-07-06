package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class AddressDeleteUseCaseImpl implements AddressDeleteUseCase {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressDeleteUseCaseImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public AddressDeleteUseCaseOutputDto execute(User user, Integer id) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var address = this.addressRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Endereço inexistente"));

        if(userLogged.getRole().equals(UserRole.CLIENT) && address.getUser().getId() != userLogged.getId())
            throw new UnauthorizedException("Você não possui autorização para deletar endereço de outro usuário");

        this.addressRepository.delete(address);

        return new AddressDeleteUseCaseOutputDto(true);
    }
}
