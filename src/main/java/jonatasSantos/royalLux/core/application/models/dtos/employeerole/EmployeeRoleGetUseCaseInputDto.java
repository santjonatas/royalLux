package jonatasSantos.royalLux.core.application.models.dtos.employeerole;

public record EmployeeRoleGetUseCaseInputDto(Integer id, Integer employeeId, Integer roleId) {
    public boolean filterIsEmpty() {
        return id == null && employeeId == null && roleId == null;
    }
}
