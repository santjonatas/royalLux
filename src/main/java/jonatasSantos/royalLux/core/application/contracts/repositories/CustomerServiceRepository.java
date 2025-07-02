package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerServiceRepository extends JpaRepository<CustomerService, String> {
    List<CustomerService> findByCreatedByUserId(Integer userId);

    List<CustomerService> findByClientId(Integer clientId);
}
