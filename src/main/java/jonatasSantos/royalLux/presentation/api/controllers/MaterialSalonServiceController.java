package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice.MaterialSalonServiceUpdateUseCaseInputDto;
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
@RequestMapping("/api/materialsSalonServices")
@RequiredArgsConstructor
public class MaterialSalonServiceController {

    @Autowired
    private MaterialSalonServiceCreateUseCase materialSalonServiceCreateUseCase;

    @Autowired
    private MaterialSalonServiceGetUseCase materialSalonServiceGetUseCase;

    @Autowired
    private MaterialSalonServiceUpdateUseCase materialSalonServiceUpdateUseCase;

    @Autowired
    private MaterialSalonServiceDeleteUseCase materialSalonServiceDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createMaterialSalonService(@RequestBody MaterialSalonServiceCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = materialSalonServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).createMaterialSalonService(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).getMaterialsSalonServices(null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).updateMaterialSalonService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).deleteMaterialSalonService(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).getMaterialsSalonServices(response.materialServiceId(), null, null, null, null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getMaterialsSalonServices(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer salonServiceId,
            @RequestParam(required = false) Integer materialId,
            @RequestParam(required = false) Integer quantityMaterial,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new MaterialSalonServiceGetUseCaseInputDto(id, salonServiceId, materialId, quantityMaterial);
        var response = materialSalonServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).createMaterialSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).getMaterialsSalonServices(null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).updateMaterialSalonService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).deleteMaterialSalonService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updateMaterialSalonService(
            @RequestParam(required = true) Integer id,
            @RequestBody MaterialSalonServiceUpdateUseCaseInputDto body){
        var response = materialSalonServiceUpdateUseCase.execute(id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).createMaterialSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).getMaterialsSalonServices(null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).updateMaterialSalonService(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).deleteMaterialSalonService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity deleteMaterialSalonService(@RequestParam(required = true) Integer id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = materialSalonServiceDeleteUseCase.execute(user, id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).createMaterialSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).getMaterialsSalonServices(null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).updateMaterialSalonService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialSalonServiceController.class).deleteMaterialSalonService(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
