package jonatasSantos.royalLux.core.application.models.dtos.employeerole;

public record EmployeeRoleDeleteUseCaseOutputDto(boolean success) {
    public EmployeeRoleDeleteUseCaseOutputDto(){
        this(false);
    }
}
