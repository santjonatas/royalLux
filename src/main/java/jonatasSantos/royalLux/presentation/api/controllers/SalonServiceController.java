package jonatasSantos.royalLux.presentation.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.salonservice.SalonServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.salonservice.SalonServiceUpdateUseCaseInputDto;
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
@RequestMapping("/api/salonServices")
@RequiredArgsConstructor
public class SalonServiceController {

    @Autowired
    private SalonServiceCreateUseCase salonServiceCreateUseCase;

    @Autowired
    private SalonServiceGetUseCase salonServiceGetUseCase;

    @Autowired
    private SalonServiceUpdateUseCase salonServiceUpdateUseCase;

    @Autowired
    private SalonServiceDeleteUseCase salonServiceDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity createSalonService(@RequestBody SalonServiceCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).createSalonService(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).updateSalonService(null, null)).withRel("put"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).deleteSalonService(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(response.salonServiceId(),  null, null, null, null, null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
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
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new SalonServiceGetUseCaseInputDto(id, name, description, estimatedTime, value);
        var response = salonServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).createSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(null, null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).updateSalonService(null, null)).withRel("put"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).deleteSalonService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity updateSalonService(
            @RequestParam(required = true) Integer id,
            @RequestBody SalonServiceUpdateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).createSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).updateSalonService(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).deleteSalonService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteSalonService(@RequestParam(required = true) Integer id) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = salonServiceDeleteUseCase.execute(user, id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).createSalonService(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).getSalonService(null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).updateSalonService(null, null)).withRel("put"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalonServiceController.class).deleteSalonService(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
