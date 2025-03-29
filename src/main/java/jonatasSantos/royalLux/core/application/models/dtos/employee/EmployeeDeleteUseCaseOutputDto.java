package jonatasSantos.royalLux.core.application.models.dtos.employee;

public record EmployeeDeleteUseCaseOutputDto(boolean success) {
    public EmployeeDeleteUseCaseOutputDto(){
        this(false);
    }
}