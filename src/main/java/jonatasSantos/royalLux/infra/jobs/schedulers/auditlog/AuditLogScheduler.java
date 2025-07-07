package jonatasSantos.royalLux.infra.jobs.schedulers.auditlog;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuditLogScheduler {



    @Scheduled(cron = "0 0 3 * * ?") // todos os dias às 3h
    public void limparLogsAntigos() {
//        logService.limparLogsAntigos();
        System.out.println("Job de limpeza de logs executado com sucesso.");
    }

}
