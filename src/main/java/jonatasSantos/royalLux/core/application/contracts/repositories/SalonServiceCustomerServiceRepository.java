package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalonServiceCustomerServiceRepository extends JpaRepository<SalonServiceCustomerService, String> {

    boolean existsByCustomerServiceId(Integer customerServiceId);

    List<SalonServiceCustomerService> deleteByCustomerServiceId(Integer customerServiceId);

    List<SalonServiceCustomerService> findByEmployeeIdAndDate(Integer employeeId, LocalDate date);

    List<SalonServiceCustomerService> findByCustomerServiceId(Integer customerServiceId);

    List<SalonServiceCustomerService> findByEmployeeId(Integer employeeId);

    List<SalonServiceCustomerService> findBySalonServiceId(Integer salonServiceId);
}
