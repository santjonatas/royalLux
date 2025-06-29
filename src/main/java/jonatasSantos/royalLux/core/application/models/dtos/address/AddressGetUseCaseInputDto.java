package jonatasSantos.royalLux.core.application.models.dtos.address;

import jonatasSantos.royalLux.core.domain.enums.AddressState;

public record AddressGetUseCaseInputDto(Integer id, Integer userId, String street, String houseNumber, String complement, String neighborhood, String city, AddressState state, String cep) {}