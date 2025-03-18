package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonGetUseCaseOutputDto(Integer id, Integer userId, String name, LocalDate dateBirth, String cpf, String phone, String email, LocalDateTime createdAt, LocalDateTime updatedAt){}