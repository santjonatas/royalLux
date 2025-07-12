package jonatasSantos.royalLux.core.application.models.dtos.employeerole;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EmployeeRoleGetUseCaseOutputDto(Integer id, Integer employeeId, Integer roleId, LocalDateTime createdAt, LocalDateTime updatedAt) implements Serializable{
    private static final long serialVersionUID = 1L;
}
