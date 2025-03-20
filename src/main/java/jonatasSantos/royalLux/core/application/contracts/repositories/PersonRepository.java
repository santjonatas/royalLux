package jonatasSantos.royalLux.core.application.contracts.repositories;

import jonatasSantos.royalLux.core.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    boolean existsByUserId(Integer userId);

    boolean existsByCpf(String cpf);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Integer id);

    boolean existsByPhoneAndIdNot(String phone, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
