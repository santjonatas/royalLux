package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.materialsalonservice.MaterialSalonServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialUpdateUseCaseInputDto;
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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createMaterialSalonService(@RequestBody MaterialSalonServiceCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = materialSalonServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).createMaterial(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).getMaterials(null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).updateMaterial(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).incrementQuantityMaterial(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).decrementQuantityMaterial(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaterialController.class).deleteMaterial(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(MaterialController.class).getMaterials(response.materialId(), null, null, null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getMaterialsSalonServices(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer salonServiceId,
            @RequestParam(required = false) Integer materialId,
            @RequestParam(required = false) Integer quantityMaterial,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new MaterialSalonServiceGetUseCaseInputDto(id, salonServiceId, materialId, quantityMaterial);
        var response = materialSalonServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updateMaterialSalonService(
            @RequestParam(required = true) Integer id,
            @RequestBody MaterialSalonServiceUpdateUseCaseInputDto body){
        var response = materialSalonServiceUpdateUseCase.execute(id, body);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }


}
