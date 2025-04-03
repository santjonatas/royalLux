package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
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
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.title;

@RestController
@RequestMapping("/api/customerService")
@RequiredArgsConstructor
public class CustomerServiceController {

    @Autowired
    private CustomerServiceCreateUseCase customerServiceCreateUseCase;

    @Autowired
    private CustomerServiceGetUseCase customerServiceGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createCustomerService(@RequestBody CustomerServiceCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = customerServiceCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).createEmployee(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).updateEmployee(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).deleteEmployee(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployees(response.employeeId(), null, null, null, null, null)
//        ).toUri();

//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getEmployees(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer createdByUserId,
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime estimatedFinishingTime,
            @RequestParam(required = false) LocalDateTime finishingTime,
            @RequestParam(required = false) BigDecimal totalValue,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new CustomerServiceGetUseCaseInputDto(id, createdByUserId, clientId, status, startTime, estimatedFinishingTime, finishingTime, totalValue, details);
        var response = customerServiceGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }

}
