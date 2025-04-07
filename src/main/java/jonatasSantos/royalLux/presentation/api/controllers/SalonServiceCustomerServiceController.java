package jonatasSantos.royalLux.presentation.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservicecustomerservice.SalonServiceCustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/salonServicesCustomerService")
@RequiredArgsConstructor
public class SalonServiceCustomerServiceController {

    @Autowired
    private SalonServiceCustomerServiceCreateUseCase salonServiceCustomerServiceCreateUseCase;

    @Autowired
    private SalonServiceCustomerServiceGetUseCase salonServiceCustomerServiceGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createSalonServiceCustomerService(@RequestBody SalonServiceCustomerServiceCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCustomerServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).createSalonService(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).updateSalonService(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).deleteSalonService(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(response.salonServiceId(),  null, null, null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getSalonServiceCustomerService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer customerServiceId,
            @RequestParam(required = false) Integer salonServiceId,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean ascending) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new SalonServiceCustomerServiceGetUseCaseInputDto(id, customerServiceId, salonServiceId, employeeId, completed);
        var response = salonServiceCustomerServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }


}
