package jonatasSantos.royalLux.core.application.usecases.user;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.*;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserDeleteUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserDeleteUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
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

    @AuditLogAnnotation
    @Override
    public UserDeleteUseCaseOutputDto execute(User user, Integer id) {
        var userToBeDeleted = this.userRepository.findById(id.toString())
                .orElseThrow(() -> new EntityNotFoundException("Usuário inexistente"));

        if(userToBeDeleted.getRole().equals(UserRole.ADMIN))
            throw new UnauthorizedException("Admin não pode ser deletado");

        var person = this.personRepository.findByUserId(userToBeDeleted.getId());
        if(person != null)
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados pessoais vinculados");

        var addresses = this.addressRepository.findByUserId(userToBeDeleted.getId());
        if(!addresses.isEmpty())
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui endereço vinculado");

        if(userToBeDeleted.getRole().equals(UserRole.EMPLOYEE)){
            var employee = this.employeeRepository.findByUserId(userToBeDeleted.getId());
            if(employee != null)
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados de funcionário vinculados");

        } else if (userToBeDeleted.getRole().equals(UserRole.CLIENT)) {
            var client = this.clientRepository.findByUserId(userToBeDeleted.getId());
            if(client != null)
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui dados de cliente vinculados");
        }

        if (!userToBeDeleted.getRole().equals(UserRole.CLIENT)){
            var customerServices = this.customerServiceRepository.findByCreatedByUserId(userToBeDeleted.getId());

            if(!customerServices.isEmpty())
                throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui atendimentos criados vinculados");
        }

        var payments = this.paymentRepository.findByCreatedByUserId(userToBeDeleted.getId());
        if(!payments.isEmpty())
            throw new IllegalStateException("Usuário não pode ser deletado pois ainda possui pagamentos vinculados");

        this.userRepository.delete(userToBeDeleted);

        return new UserDeleteUseCaseOutputDto(true);
    }
}