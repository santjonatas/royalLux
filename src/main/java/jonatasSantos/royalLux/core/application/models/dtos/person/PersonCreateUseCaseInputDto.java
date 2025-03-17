package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.time.LocalDate;

public record PersonCreateUseCaseInputDto(Integer userId, String name, LocalDate dateBirth, String cpf, String phone, String email){}
