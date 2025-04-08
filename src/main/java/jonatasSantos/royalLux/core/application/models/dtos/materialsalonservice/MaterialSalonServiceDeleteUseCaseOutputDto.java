package jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice;

public record MaterialSalonServiceDeleteUseCaseOutputDto(boolean success) {
    public MaterialSalonServiceDeleteUseCaseOutputDto(){
        this(false);
    }
}
