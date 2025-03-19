package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.time.LocalDate;

public record PersonUpdateUseCaseInputDto(String name, LocalDate dateBirth, String cpf, String phone, String email){}
