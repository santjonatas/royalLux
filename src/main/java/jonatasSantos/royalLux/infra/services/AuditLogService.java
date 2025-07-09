package jonatasSantos.royalLux.infra.services;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jonatasSantos.royalLux.core.application.contracts.services.SensitiveDataMasker;
import jonatasSantos.royalLux.core.domain.entities.AuditLog;
import jonatasSantos.royalLux.core.domain.enums.AuditLogStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


@Aspect @Component public class AuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    private final jonatasSantos.royalLux.core.application.contracts.repositories.AuditLogRepository auditLogRepository;
    private final SensitiveDataMasker sensitiveDataMasker;

    public AuditLogService(jonatasSantos.royalLux.core.application.contracts.repositories.AuditLogRepository auditLogRepository, SensitiveDataMasker sensitiveDataMasker) {
        this.auditLogRepository = auditLogRepository;
        this.sensitiveDataMasker = sensitiveDataMasker;
    }

    @Around("@annotation(jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation)")
    public Object logUseCaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        AuditLog log = new AuditLog();
        log.setOrigin(signature.getDeclaringTypeName());
        log.setMethod(signature.getMethod().getName());
        log.setCreatedAt(start);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            log.setStatus(AuditLogStatus.SUCESSO);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String resultJson = mapper.writeValueAsString(result);
            log.setResult(result != null ? sensitiveDataMasker.maskSensitiveFields(resultJson) : null);

            logger.info("Sucesso no método [{}] | Resultado: {}", log.getOrigin() + "." + log.getMethod(), log.getResult());
            return result;

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException ||
                    e instanceof jakarta.persistence.EntityNotFoundException ||
                    e instanceof jakarta.persistence.EntityExistsException ||
                    e instanceof org.springframework.security.core.userdetails.UsernameNotFoundException ||
                    e instanceof javax.management.relation.RoleNotFoundException ||
                    e instanceof org.springframework.http.converter.HttpMessageNotReadableException ||
                    e instanceof javax.naming.AuthenticationException ||
                    e instanceof org.springframework.security.access.AccessDeniedException ||
                    e instanceof org.springframework.security.authentication.DisabledException ||
                    e instanceof org.springframework.security.authentication.CredentialsExpiredException) {

                log.setStatus(AuditLogStatus.ERRO_NEGOCIO);
                log.setStackTrace(Arrays.toString(e.getStackTrace()));
                logger.warn("Erro de negócio no método [{}]: {}", log.getOrigin() + "." + log.getMethod(), e.getMessage());

            } else {
                log.setStatus(AuditLogStatus.ERRO);
                log.setStackTrace(Arrays.toString(e.getStackTrace()));
                logger.error(" Erro técnico no método [{}]: {}", log.getOrigin() + "." + log.getMethod(), e.getMessage(), e);
            }

            log.setDescription(e.getMessage());
            throw e;
        } finally {
            int duration = (int) (System.currentTimeMillis() - startTime);
            log.setExecutionTimeMs(duration);

            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof jonatasSantos.royalLux.core.domain.entities.User user) {
                    log.setUserId(user.getId());
                    break;
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                String parametersJson = mapper.writeValueAsString(joinPoint.getArgs());
                log.setParameters(sensitiveDataMasker.maskSensitiveFields(parametersJson));
            } catch (JsonProcessingException ex) {
                log.setParameters("Erro ao serializar parâmetros");
            }

            auditLogRepository.save(log);

            logger.info("Log persistido — [{}] | Status: {} | Duração: {}ms",
                    log.getMethod(), log.getStatus(), duration);
        }
    }
}
