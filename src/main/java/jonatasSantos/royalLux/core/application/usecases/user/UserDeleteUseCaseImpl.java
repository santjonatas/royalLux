package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteUseCaseImpl implements UserDeleteUseCase {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final PaymentRepository paymentRepository;
    private final AddressRepository addressRepository;

    public UserDeleteUseCaseImpl(UserRepository userRepository, PersonRepository personRepository, ClientRepository clientRepository, EmployeeRepository employeeRepository, CustomerServiceRepository customerServiceRepository, PaymentRepository paymentRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.paymentRepository = paymentRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public UserDeleteUseCaseOutputDto execute(Integer id) {
        var user = this.userRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        if(user.getRole().equals(UserRole.ADMIN))
            throw new UnauthorizedException("Admin não pode ser deletado");

        var person = this.personRepository.findByUserId(user.getId());
        if(person != null)
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados pessoais vinculados");

        var addresses = this.addressRepository.findByUserId(user.getId());
        if(!addresses.isEmpty())
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui endereço vinculado");

        if(user.getRole().equals(UserRole.EMPLOYEE)){
            var employee = this.employeeRepository.findByUserId(user.getId());
            if(employee != null)
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados de funcionário vinculados");

        } else if (user.getRole().equals(UserRole.CLIENT)) {
            var client = this.clientRepository.findByUserId(user.getId());
            if(client != null)
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados de cliente vinculados");
        }

        if (!user.getRole().equals(UserRole.CLIENT)){
            var customerServices = this.customerServiceRepository.findByCreatedByUserId(user.getId());

            if(!customerServices.isEmpty())
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui atendimentos criados vinculados");
        }

        var payments = this.paymentRepository.findByCreatedByUserId(user.getId());
        if(!payments.isEmpty())
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui pagamentos vinculados");

        this.userRepository.delete(user);

        return new UserDeleteUseCaseOutputDto(true);
    }
}