package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}