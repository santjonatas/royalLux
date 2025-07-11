package jonatasSantos.royalLux.core.application.contracts.usecases.auditlog;

import jonatasSantos.royalLux.core.application.models.dtos.auditlog.AuditLogGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auditlog.AuditLogGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;

import java.util.List;

public interface AuditLogGetUseCase {
    public List<AuditLogGetUseCaseOutputDto> execute(User user, AuditLogGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending);
}
