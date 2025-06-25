package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.services.AuthCodeService;
import jonatasSantos.royalLux.core.application.contracts.services.MessagePublisher;
import jonatasSantos.royalLux.core.application.contracts.services.SerializerService;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserSendPasswordRecoveryCodeUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserSendPasswordRecoveryCodeUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.enums.CodeDeliveryChannel;
import org.springframework.stereotype.Service;

@Service
public class UserSendPasswordRecoveryCodeUseCaseImpl implements UserSendPasswordRecoveryCodeUseCase {

    private final UserRepository userRepository;
    private final MessagePublisher notificationPublisher;
    private final AuthCodeService authCodeService;
    private final SerializerService serializerService;

    public UserSendPasswordRecoveryCodeUseCaseImpl(UserRepository userRepository, MessagePublisher notificationPublisher, AuthCodeService authCodeService, SerializerService serializerService) {
        this.userRepository = userRepository;
        this.notificationPublisher = notificationPublisher;
        this.authCodeService = authCodeService;
        this.serializerService = serializerService;
    }

    @Override
    public UserSendPasswordRecoveryCodeUseCaseOutputDto execute(UserSendPasswordRecoveryCodeUseCaseInputDto input){

        var user = this.userRepository.findByUsername(input.username())
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        String code = this.serializerService.toJson(this.authCodeService.generateCode(300));

        if(input.deliveryChannel().equals(CodeDeliveryChannel.EMAIL)){
            this.notificationPublisher.sendToEmailQueue(code);
        }
        else if (input.deliveryChannel().equals(CodeDeliveryChannel.WHATSAPP)) {
            this.notificationPublisher.sendToWhatsappQueue(code);
        }

        return new UserSendPasswordRecoveryCodeUseCaseOutputDto(true);
    }
}
