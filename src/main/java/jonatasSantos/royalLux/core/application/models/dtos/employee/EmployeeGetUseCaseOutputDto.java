package jonatasSantos.royalLux.core.application.models.dtos.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeGetUseCaseOutputDto(Integer id, Integer userId, String title, BigDecimal salary, LocalDateTime createdAt, LocalDateTime updatedAt) implements Serializable{
    private static final long serialVersionUID = 1L;
}