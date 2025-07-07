package jonatasSantos.royalLux.infra.jobs.schedulers.auditlog;

import jonatasSantos.royalLux.core.application.contracts.repositories.AuditLogRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.SchedulerConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuditLogScheduler {

    @Value("${scheduler.name.audit.log.delete}")
    private String auditLogDeleteSchedulerName;

    private final SchedulerConfigRepository schedulerConfigRepository;
    private final AuditLogRepository auditLogRepository;

    public AuditLogScheduler(SchedulerConfigRepository schedulerConfigRepository, AuditLogRepository auditLogRepository) {
        this.schedulerConfigRepository = schedulerConfigRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?") // todos os dias às 3h
    public void limparLogsAntigos() {
        var auditLogDeleteScheduler = schedulerConfigRepository.findByName(auditLogDeleteSchedulerName);
        if (auditLogDeleteScheduler != null && auditLogDeleteScheduler.getDate() != null) {
            if(auditLogDeleteScheduler.getDate().isEqual(LocalDate.now()) && auditLogDeleteScheduler.getEnabled())
                this.auditLogRepository.deleteAll();

            System.out.println("Job de limpeza de logs executado com sucesso.");
        }
    }

}
