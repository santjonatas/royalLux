package jonatasSantos.royalLux.core.application.models.dtos.auth;

public record UserResetPasswordUseCaseInputDto(String username, String newPassword, String code) {
}
