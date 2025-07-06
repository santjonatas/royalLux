package jonatasSantos.royalLux.core.application.usecases.employee;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.annotations.AuditLogAnnotation;
import jonatasSantos.royalLux.core.application.contracts.repositories.EmployeeRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeUpdateUseCase;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class EmployeeUpdateUseCaseImpl implements EmployeeUpdateUseCase {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeUpdateUseCaseImpl(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @AuditLogAnnotation
    @Override
    public EmployeeUpdateUseCaseOutputDto execute(User user, Integer employeeId, EmployeeUpdateUseCaseInputDto input) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        var employeeToBeUpdated = this.employeeRepository.findById(String.valueOf(employeeId))
                .orElseThrow(() -> new EntityNotFoundException("Funcionário é inexistente"));

        ArrayList<String> warningList = new ArrayList<>();

        if(userLogged.getRole().equals(UserRole.ADMIN)){
            employeeToBeUpdated.setTitle(input.title());
            employeeToBeUpdated.setSalary(input.salary());
        }

        else if(userLogged.getRole().equals(UserRole.EMPLOYEE)){
            if(employeeToBeUpdated.getUser().getId() != userLogged.getId())
                throw new UnauthorizedException("Você não possui autorização para atualizar outro funcionário");

            employeeToBeUpdated.setTitle(input.title());

            if(!employeeToBeUpdated.getSalary().equals(input.salary()))
                warningList.add("Você não possui autorização para atualizar o salário");
        }

        employeeToBeUpdated.setUpdatedAt(LocalDateTime.now());
        this.employeeRepository.save(employeeToBeUpdated);

        return new EmployeeUpdateUseCaseOutputDto(true, warningList);
    }
}