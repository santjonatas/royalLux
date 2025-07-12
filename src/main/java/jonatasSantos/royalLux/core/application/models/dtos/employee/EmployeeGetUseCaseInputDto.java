package jonatasSantos.royalLux.core.application.models.dtos.employee;

import java.math.BigDecimal;

public record EmployeeGetUseCaseInputDto(Integer id, Integer userId, String title, BigDecimal salary){
    public boolean filterIsEmpty() {
        return id == null && userId == null && title == null && salary == null;
    }
}