package jonatasSantos.royalLux.core.application.models.dtos.auditlog;

import jonatasSantos.royalLux.core.domain.enums.AuditLogStatus;

import java.time.LocalDateTime;

public record AuditLogGetUseCaseOutputDto(Integer id, Integer userId, String origin, String method, String parameters, String result, String description, String stackTrace, AuditLogStatus status, LocalDateTime createdAt) {
}
