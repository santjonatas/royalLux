package jonatasSantos.royalLux.core.application.models.dtos.employee;

import java.math.BigDecimal;

public record EmployeeCreateUseCaseInputDto(Integer userId, String title, BigDecimal salary){}