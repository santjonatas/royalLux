package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.util.Date;

public record PersonGetUseCaseInputDto(Integer id, Integer userId, String name, Date dateBirth, String cpf, String phone, String email){}