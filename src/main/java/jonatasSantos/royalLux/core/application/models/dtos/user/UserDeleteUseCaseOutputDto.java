package jonatasSantos.royalLux.core.application.models.dtos.user;

public record UserDeleteUseCaseOutputDto(boolean success) {
    public UserDeleteUseCaseOutputDto(){
        this(false);
    }
}
