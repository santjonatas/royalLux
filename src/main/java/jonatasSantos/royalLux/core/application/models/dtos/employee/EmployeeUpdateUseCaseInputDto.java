package jonatasSantos.royalLux.core.application.models.dtos.employee;

import java.math.BigDecimal;

public record EmployeeUpdateUseCaseInputDto(String title, BigDecimal salary){}