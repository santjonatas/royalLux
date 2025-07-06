package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.AuditLogStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditLog")
public class AuditLog {

    public AuditLog(String origin, String method, String parameters, String result, String description, AuditLogStatus status, Integer executionTimeMs) {
        this.setOrigin(origin);
        this.setMethod(method);
        this.setParameters(parameters);
        this.setResult(result);
        this.setDescription(description);
        this.setStatus(status);
        this.setExecutionTimeMs(executionTimeMs);
        this.createdAt = LocalDateTime.now();
    }

    public AuditLog() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "userId", nullable = true)
    protected Integer userId;

    @Column(name = "origin", nullable = false)
    protected String origin;

    @Column(name = "method", nullable = false)
    protected String method;

    @Column(name = "parameters", columnDefinition = "TEXT")
    protected String parameters;

    @Column(name = "result", columnDefinition = "TEXT")
    protected String result;

    @Column(name = "description", columnDefinition = "TEXT")
    protected String description;

    @Column(name = "stackTrace", columnDefinition = "TEXT")
    protected String stackTrace;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    protected AuditLogStatus status;

    @Column(name = "executionTimeMs", nullable = false)
    protected Integer executionTimeMs;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    public Integer getId() { return id; }

    public String getOrigin() { return origin; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public void setOrigin(String origin) {
        if (origin == null || origin.isBlank())
            throw new IllegalArgumentException("Origem do log não pode ser nula ou vazia");

        this.origin = origin;
    }

    public String getMethod() { return method; }

    public void setMethod(String method) {
        if (method == null || method.isBlank())
            throw new IllegalArgumentException("Método não pode ser nulo ou vazio");

        this.method = method;
    }

    public String getParameters() { return parameters; }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() { return result; }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStackTrace() { return stackTrace; }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public AuditLogStatus getStatus() { return status; }

    public void setStatus(AuditLogStatus status) {
        if (status == null)
            throw new IllegalArgumentException("Status do log não pode ser nulo");

        this.status = status;
    }

    public Integer getExecutionTimeMs() { return executionTimeMs; }

    public void setExecutionTimeMs(Integer executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}