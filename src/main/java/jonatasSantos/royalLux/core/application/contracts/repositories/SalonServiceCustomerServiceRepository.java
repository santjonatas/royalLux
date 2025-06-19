package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.SalonServiceCustomerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalonServiceCustomerServiceRepository extends JpaRepository<SalonServiceCustomerService, String> {

    boolean existsByCustomerServiceId(Integer customerServiceId);

    List<SalonServiceCustomerService> deleteByCustomerServiceId(Integer customerServiceId);

}
