package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/employeesRoles")
@RequiredArgsConstructor
public class EmployeeRoleController {

    @Autowired
    private EmployeeRoleCreateUseCase employeeRoleCreateUseCase;

    @Autowired
    private EmployeeRoleGetUseCase employeeRoleGetUseCase;

    @Autowired
    private EmployeeRoleDeleteUseCase employeeRoleDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity createEmployeeRole(@RequestBody EmployeeRoleCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = employeeRoleCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).createEmployeeRole(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).getEmployeesRoles(null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).deleteEmployeeRole(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).getEmployeesRoles(response.employeeRoleId(), null, null, null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getEmployeesRoles(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new EmployeeRoleGetUseCaseInputDto(id, employeeId, roleId);
        var response = employeeRoleGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).createEmployeeRole(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).getEmployeesRoles(null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).deleteEmployeeRole(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteEmployeeRole(@RequestParam(required = true) Integer id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = employeeRoleDeleteUseCase.execute(user, id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).createEmployeeRole(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).getEmployeesRoles(null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeRoleController.class).deleteEmployeeRole(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
