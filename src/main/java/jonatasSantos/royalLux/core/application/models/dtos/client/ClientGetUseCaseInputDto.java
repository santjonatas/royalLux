package jonatasSantos.royalLux.core.application.models.dtos.client;

public record ClientGetUseCaseInputDto(Integer id, Integer userId){
    public boolean filterIsEmpty() {
        return id == null && userId == null;
    }
}