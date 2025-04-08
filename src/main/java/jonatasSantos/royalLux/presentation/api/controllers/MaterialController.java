package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.role.RoleGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    @Autowired
    private MaterialCreateUseCase materialCreateUseCase;

    @Autowired
    private MaterialGetUseCase materialGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createMaterial(@RequestBody MaterialCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = materialCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).createRole(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).updateRole(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).deleteRole(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(response.roleId(), null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getMaterials(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal value,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new MaterialGetUseCaseInputDto(id, name, description, value, quantity);
        var response = materialGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}
