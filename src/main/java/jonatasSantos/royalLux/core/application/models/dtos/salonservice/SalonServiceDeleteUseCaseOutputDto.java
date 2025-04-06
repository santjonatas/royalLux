package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

public record SalonServiceDeleteUseCaseOutputDto(boolean success) {
    public SalonServiceDeleteUseCaseOutputDto(){
        this(false);
    }
}
