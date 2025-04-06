package jonatasSantos.royalLux.presentation.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/salonServices")
@RequiredArgsConstructor
public class SalonServiceController {

    @Autowired
    private SalonServiceCreateUseCase salonServiceCreateUseCase;

    @Autowired
    private SalonServiceGetUseCase salonServiceGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity createSalonService(@RequestBody SalonServiceCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).createCustomerService(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).getCustomerService(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).updateCustomerService(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).deleteCustomerService(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(CustomerServiceController.class).getCustomerService(response.customerServiceId(),  null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getSalonService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @Parameter(
                    description = "Tempo estimado no formato HH:mm",
                    schema = @Schema(type = "string", pattern = "HH:mm")
            )
            @DateTimeFormat(pattern = "HH:mm")
            @RequestParam(required = false) LocalTime estimatedTime,
            @RequestParam(required = false) BigDecimal value,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new SalonServiceGetUseCaseInputDto(id, name, description, estimatedTime, value);
        var response = salonServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }

}
