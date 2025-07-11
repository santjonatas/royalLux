package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig.SchedulerConfigGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.schedulerconfig.SchedulerConfigUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.schedulerconfig.SchedulerConfigGetUseCaseInputDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedulersConfig")
@RequiredArgsConstructor
public class SchedulerConfigController {

    @Autowired
    private SchedulerConfigGetUseCase schedulerConfigGetUseCase;

    @Autowired
    private SchedulerConfigUpdateUseCase schedulerConfigUpdateUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getSchedulerConfig(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new SchedulerConfigGetUseCaseInputDto(id, name, description, enabled, year, month, day);
        var response = schedulerConfigGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SchedulerConfigController.class).getSchedulerConfig(null, null, null, null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SchedulerConfigController.class).updateSchedulerConfig(null, null)).withRel("patch"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity updateSchedulerConfig(
            @RequestParam(required = true) Integer id,
            @RequestBody SchedulerConfigUpdateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = schedulerConfigUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SchedulerConfigController.class).getSchedulerConfig(null, null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SchedulerConfigController.class).updateSchedulerConfig(null, null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
