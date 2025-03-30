package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AddressUpdateUseCaseImpl implements AddressUpdateUseCase {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressUpdateUseCaseImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressUpdateUseCaseOutputDto execute(User user, Integer addressId, AddressUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var addressToBeUpdated = this.addressRepository.findById(String.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Endereço é inexistente"));

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.EMPLOYEE)
                && addressToBeUpdated.getUser().getId() != userLogged.getId()){
            throw new UnauthorizedException("Você não possui autorização para atualizar endereço de outro usuário");
        }

        if(userLogged.getRole().equals(UserRole.CLIENT)
                && addressToBeUpdated.getUser().getId() != userLogged.getId()){
            throw new UnauthorizedException("Você não possui autorização para atualizar endereço de outro usuário");
        }

        addressToBeUpdated.setStreet(input.street());
        addressToBeUpdated.setHouseNumber(input.houseNumber());
        addressToBeUpdated.setComplement(input.complement());
        addressToBeUpdated.setNeighborhood(input.neighborhood());
        addressToBeUpdated.setCity(input.city());
        addressToBeUpdated.setState(input.state());
        addressToBeUpdated.setCep(input.cep());
        addressToBeUpdated.setUpdatedAt(LocalDateTime.now());

        this.addressRepository.save(addressToBeUpdated);

        return new AddressUpdateUseCaseOutputDto(true, warningList);
    }
}
