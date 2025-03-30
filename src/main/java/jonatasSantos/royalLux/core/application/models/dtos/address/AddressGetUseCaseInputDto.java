package jonatasSantos.royalLux.core.application.models.dtos.address;

import jonatasSantos.royalLux.core.domain.enums.AddressStates;

public record AddressGetUseCaseInputDto(Integer id, Integer userId, String street, String houseNumber, String complement, String neighborhood, String city, AddressStates state, String cep) {}