package jonatasSantos.royalLux.core.application.models.dtos.auditlog;

import jonatasSantos.royalLux.core.domain.enums.AuditLogStatus;

public record AuditLogGetUseCaseInputDto(Integer id, Integer userId, String origin, String method, String parameters, String result, String description, String stackTrace, AuditLogStatus status, Integer year, Integer month, Integer day, Integer hour, Integer minute) {
}
