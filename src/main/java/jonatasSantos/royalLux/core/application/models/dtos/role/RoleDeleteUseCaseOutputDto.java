package jonatasSantos.royalLux.core.application.models.dtos.role;

public record RoleDeleteUseCaseOutputDto(boolean success) {
    public RoleDeleteUseCaseOutputDto(){
        this(false);
    }
}
