package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeCreateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Employee;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class EmployeeCreateUseCaseImpl implements EmployeeCreateUseCase {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeCreateUseCaseImpl(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public EmployeeCreateUseCaseOutputDto execute(User user, EmployeeCreateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var existingUser = this.userRepository.findById(String.valueOf(input.userId()))
                .orElseThrow(() -> new EntityNotFoundException("Usuário é inexistente"));

        if(existingUser.getRole() != UserRole.EMPLOYEE)
            throw new IllegalArgumentException("Usuário deve ser um funcionário");

        if(this.employeeRepository.existsByUserId(existingUser.getId()))
            throw new ConflictException("Funcionário já vinculado a um usuário");

        Employee employee = new Employee(existingUser, input.title(), input.salary());

        this.employeeRepository.save(employee);

        return new EmployeeCreateUseCaseOutputDto(employee.getId());
    }
}
