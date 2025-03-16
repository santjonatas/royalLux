package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.util.Date;

public record PersonCreateUseCaseInputDto(Integer userId, String name, Date dateBirth, String cpf, String phone, String email){}
