package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.MaterialSalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialSalonServiceRepository extends JpaRepository<MaterialSalonService, String> {
    boolean existsByMaterialIdAndSalonServiceId(Integer materialId, Integer salonServiceId);

    List<MaterialSalonService> findBySalonServiceId(Integer salonServiceId);
}
