package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Address;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.AddressState;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AddressCreateUseCaseImpl implements AddressCreateUseCase {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressCreateUseCaseImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public AddressCreateUseCaseOutputDto execute(User user, AddressCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var existingUser = this.userRepository.findById(String.valueOf(input.userId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário é inexistente"));

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            if(!Objects.equals(existingUser.getId(), userLogged.getId()))
                throw new UnauthorizedException("Você não possui autorização para criar endereço de outro usuário");
        }

        if(userLogged.getRole().equals(UserRole.CLIENT)){
            if(!Objects.equals(existingUser.getId(), userLogged.getId()))
                throw new UnauthorizedException("Você não possui autorização para criar endereço de outro usuário");
        }

        if (!AddressState.STATES.contains(input.state()))
            throw new IllegalArgumentException("Estado inválido");

        Address address = new Address(
                existingUser,
                input.street(),
                input.houseNumber(),
                input.complement(),
                input.neighborhood(),
                input.city(),
                input.state(),
                input.cep()
        );

        this.addressRepository.save(address);

        return new AddressCreateUseCaseOutputDto(address.getId());
    }
}