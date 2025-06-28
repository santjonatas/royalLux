package jonatasSantos.royalLux.core.application.usecases.auth;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.services.AuthCodeService;
import jonatasSantos.royalLux.core.application.contracts.services.messagebroker.publishers.MessagePublisher;
import jonatasSantos.royalLux.core.application.contracts.services.SerializerService;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.UserSendPasswordRecoveryCodeUseCase;
import jonatasSantos.royalLux.core.application.mappers.UserAuthCodeMapper;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseOutputDto;
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

        var authCode = this.authCodeService.generateCode(300);
        String code = this.serializerService.toJson(new UserAuthCodeMapper(user.getUsername(), authCode.code()));

        if(input.deliveryChannel().equals(CodeDeliveryChannel.EMAIL)){
            this.notificationPublisher.sendToEmailQueue(code);
        }

        return new UserSendPasswordRecoveryCodeUseCaseOutputDto(true);
    }
}
