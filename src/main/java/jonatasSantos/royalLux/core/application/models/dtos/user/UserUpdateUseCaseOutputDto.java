package jonatasSantos.royalLux.core.application.models.dtos.user;

public record UserUpdateUseCaseOutputDto (boolean success){
    public UserUpdateUseCaseOutputDto() {
        this(false);
    }
}