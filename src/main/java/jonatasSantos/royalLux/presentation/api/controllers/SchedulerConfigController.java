package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig.SchedulerConfigUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedulersConfig")
@RequiredArgsConstructor
public class SchedulerConfigController {

    @Autowired
    private SchedulerConfigUpdateUseCase schedulerConfigUpdateUseCase;

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity updateSchedulerConfig(
            @RequestParam(required = true) Integer id,
            @RequestBody SchedulerConfigUpdateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = schedulerConfigUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).createMaterial(null)).withRel("post"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).getMaterials(null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).updateMaterial(null, null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).incrementQuantityMaterial(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).decrementQuantityMaterial(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).deleteMaterial(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }
}
