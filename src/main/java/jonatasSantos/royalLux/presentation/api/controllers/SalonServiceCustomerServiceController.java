package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceUpdateUseCaseInputDto;
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
import java.net.URI;

@RestController
@RequestMapping("/api/salonServicesCustomerService")
@RequiredArgsConstructor
public class SalonServiceCustomerServiceController {

    @Autowired
    private SalonServiceCustomerServiceCreateUseCase salonServiceCustomerServiceCreateUseCase;

    @Autowired
    private SalonServiceCustomerServiceGetUseCase salonServiceCustomerServiceGetUseCase;

    @Autowired
    private SalonServiceCustomerServiceUpdateUseCase salonServiceCustomerServiceUpdateUseCase;

    @Autowired
    private SalonServiceCustomerServiceDeleteUseCase salonServiceCustomerServiceDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createSalonServiceCustomerService(@RequestBody SalonServiceCustomerServiceCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCustomerServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).createSalonServiceCustomerService(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).getSalonServiceCustomerService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).updateSalonServiceCustomerService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).deleteSalonServiceCustomerService(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).getSalonServiceCustomerService(response.salonServiceCustomerServiceId(),  null, null, null, null, null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getSalonServiceCustomerService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer customerServiceId,
            @RequestParam(required = false) Integer salonServiceId,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new SalonServiceCustomerServiceGetUseCaseInputDto(id, customerServiceId, salonServiceId, employeeId, completed);
        var response = salonServiceCustomerServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).createSalonServiceCustomerService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).getSalonServiceCustomerService(null, null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).updateSalonServiceCustomerService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).deleteSalonServiceCustomerService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updateSalonServiceCustomerService(
            @RequestParam(required = true) Integer id,
            @RequestBody SalonServiceCustomerServiceUpdateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCustomerServiceUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).createSalonServiceCustomerService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).getSalonServiceCustomerService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).updateSalonServiceCustomerService(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).deleteSalonServiceCustomerService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteSalonServiceCustomerService(@RequestParam(required = true) Integer id) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCustomerServiceDeleteUseCase.execute(user, id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).createSalonServiceCustomerService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).getSalonServiceCustomerService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).updateSalonServiceCustomerService(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceCustomerServiceController.class).deleteSalonServiceCustomerService(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
