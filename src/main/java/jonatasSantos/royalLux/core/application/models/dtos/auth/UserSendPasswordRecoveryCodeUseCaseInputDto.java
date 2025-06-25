package jonatasSantos.royalLux.core.application.models.dtos.auth;

import jonatasSantos.royalLux.core.domain.enums.CodeDeliveryChannel;

public record UserSendPasswordRecoveryCodeUseCaseInputDto(String username, CodeDeliveryChannel deliveryChannel) {
}
