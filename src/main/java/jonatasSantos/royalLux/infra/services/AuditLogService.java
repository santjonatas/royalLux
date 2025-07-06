package jonatasSantos.royalLux.infra.services;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Around("@annotation(jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation)")
    public Object logUseCaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            logger.info("Método [{}] executado com sucesso. Retorno: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Exception e) {
            logger.error("Erro durante a execução do método [{}]. Motivo: {}", joinPoint.getSignature().toShortString(), e.getMessage(), e);
            throw e;
        }
    }
}
