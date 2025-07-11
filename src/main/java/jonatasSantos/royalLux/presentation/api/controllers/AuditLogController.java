package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.auditlog.AuditLogGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auditlog.AuditLogGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.AddressState;
import jonatasSantos.royalLux.core.domain.enums.AuditLogStatus;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditLogs")
@RequiredArgsConstructor
public class AuditLogController {

    @Autowired
    private AuditLogGetUseCase auditLogGetUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getAuditLogs(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String parameters,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String stackTrace,
            @RequestParam(required = false) AuditLogStatus status,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer hour,
            @RequestParam(required = false) Integer minute,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new AuditLogGetUseCaseInputDto(id, userId, origin, method, parameters, result, description, stackTrace, status, year, month, day, hour, minute);
        var response = auditLogGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuditLogController.class).getAuditLogs(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)).withSelfRel());
        return ResponseEntity.ok(responsePresenter);
    }
}
