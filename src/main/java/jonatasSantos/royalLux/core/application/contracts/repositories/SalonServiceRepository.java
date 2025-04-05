package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonServiceRepository extends JpaRepository<SalonService, String> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
