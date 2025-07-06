package jonatasSantos.royalLux.core.application.contracts.repositories;


import jonatasSantos.royalLux.core.domain.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}
