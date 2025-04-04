package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

public record CustomerServiceDeleteUseCaseOutputDto(boolean success) {
    public CustomerServiceDeleteUseCaseOutputDto(){
        this(false);
    }
}
