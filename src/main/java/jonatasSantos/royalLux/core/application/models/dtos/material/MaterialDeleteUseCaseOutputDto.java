package jonatasSantos.royalLux.core.application.models.dtos.material;

public record MaterialDeleteUseCaseOutputDto(boolean success) {
    public MaterialDeleteUseCaseOutputDto(){
        this(false);
    }
}
