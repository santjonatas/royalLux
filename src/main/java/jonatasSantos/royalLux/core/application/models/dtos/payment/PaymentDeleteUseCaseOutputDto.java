package jonatasSantos.royalLux.core.application.models.dtos.payment;

public record PaymentDeleteUseCaseOutputDto(boolean success) {
    public PaymentDeleteUseCaseOutputDto(){
        this(false);
    }
}
