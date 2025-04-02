package jonatasSantos.royalLux.core.application.models.dtos.employeerole;

import java.time.LocalDateTime;

public record EmployeeRoleGetUseCaseOutputDto(Integer id, Integer employeeId, Integer roleId, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
