package jonatasSantos.royalLux.core.application.models.dtos.address;

public record AddressDeleteUseCaseOutputDto(boolean success) {
    public AddressDeleteUseCaseOutputDto(){
        this(false);
    }
}
