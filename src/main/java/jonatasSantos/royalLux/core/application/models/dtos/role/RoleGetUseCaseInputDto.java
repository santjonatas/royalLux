package jonatasSantos.royalLux.core.application.models.dtos.role;

public record RoleGetUseCaseInputDto(Integer id, String name, String detail) {
    public boolean filterIsEmpty() {
        return id == null && name == null && detail == null;
    }
}
