package jonatasSantos.royalLux.core.application.models.dtos.address;

import jonatasSantos.royalLux.core.domain.enums.AddressState;
import java.time.LocalDateTime;

public record AddressGetUseCaseOutputDto(Integer id, Integer userId, String street, String houseNumber, String complement, String neighborhood, String city, AddressState state, String cep, LocalDateTime createdAt, LocalDateTime updatedAt) {}