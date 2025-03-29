package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.employee.EmployeeUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeCreateUseCase employeeCreateUseCase;

    @Autowired
    private EmployeeGetUseCase employeeGetUseCase;

    @Autowired
    private EmployeeUpdateUseCase employeeUpdateUseCase;

    @Autowired
    private EmployeeDeleteUseCase employeeDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity createEmployee(@RequestBody EmployeeCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = employeeCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).createEmployee(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).updateEmployee(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).deleteEmployee(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getEmployees(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal salary,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new EmployeeGetUseCaseInputDto(id, userId, title, salary);
        var response = employeeGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).createEmployee(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).updateEmployee(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).deleteEmployee(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updateEmployee(
            @RequestParam(required = true) Integer id,
            @RequestBody EmployeeUpdateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = employeeUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).createEmployee(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).updateEmployee(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).deleteEmployee(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteEmployee(@RequestParam(required = true) Integer id) throws AuthenticationException {
        var response = employeeDeleteUseCase.execute(id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).createEmployee(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).updateEmployee(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).deleteEmployee(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}