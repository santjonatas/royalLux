package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, String> {
    boolean existsByEmployeeIdAndRoleId(Integer employeeId, Integer roleId);
}
