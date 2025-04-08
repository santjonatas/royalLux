package jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice;

import java.time.LocalDateTime;

public record MaterialSalonServiceGetUseCaseOutputDto(Integer id, Integer salonServiceId, Integer materialId, Integer quantityMaterial, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
