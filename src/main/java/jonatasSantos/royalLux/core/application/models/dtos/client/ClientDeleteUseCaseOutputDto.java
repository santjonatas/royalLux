package jonatasSantos.royalLux.core.application.models.dtos.client;

public record ClientDeleteUseCaseOutputDto(boolean success) {
    public ClientDeleteUseCaseOutputDto(){
        this(false);
    }
}