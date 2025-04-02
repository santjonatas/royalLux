package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.employeerole.EmployeeRoleCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.employeerole.EmployeeRoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/employeesRoles")
@RequiredArgsConstructor
public class EmployeeRoleController {

    @Autowired
    private EmployeeRoleCreateUseCase employeeRoleCreateUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity createEmployeeRole(@RequestBody EmployeeRoleCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = employeeRoleCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).createRole(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).updateRole(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).deleteRole(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(response.roleId(), null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }
}
