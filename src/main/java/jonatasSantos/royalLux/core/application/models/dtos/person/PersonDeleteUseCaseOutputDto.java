package jonatasSantos.royalLux.core.application.models.dtos.person;

public record PersonDeleteUseCaseOutputDto(boolean success) {
    public PersonDeleteUseCaseOutputDto(){
        this(false);
    }
}