package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.customerservice.CustomerServiceUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.employee.EmployeeUpdateUseCaseInputDto;
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

    @Autowired
    private CustomerServiceUpdateUseCase customerServiceUpdateUseCase;

    @Autowired
    private CustomerServiceDeleteUseCase customerServiceDeleteUseCase;

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
    public ResponseEntity getCustomerService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer createdByUserId,
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer startTimeYear,
            @RequestParam(required = false) Integer startTimeMonth,
            @RequestParam(required = false) Integer startTimeDay,
            @RequestParam(required = false) Integer startTimeHour,
            @RequestParam(required = false) Integer startTimeMinute,
            @RequestParam(required = false) Integer estimatedFinishingTimeYear,
            @RequestParam(required = false) Integer estimatedFinishingTimeMonth,
            @RequestParam(required = false) Integer estimatedFinishingTimeDay,
            @RequestParam(required = false) Integer estimatedFinishingTimeHour,
            @RequestParam(required = false) Integer estimatedFinishingTimeMinute,
            @RequestParam(required = false) Integer finishingTimeYear,
            @RequestParam(required = false) Integer finishingTimeMonth,
            @RequestParam(required = false) Integer finishingTimeDay,
            @RequestParam(required = false) Integer finishingTimeHour,
            @RequestParam(required = false) Integer finishingTimeMinute,
            @RequestParam(required = false) BigDecimal totalValue,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean ascending) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new CustomerServiceGetUseCaseInputDto(id,
                createdByUserId,
                clientId,
                status,
                startTimeYear,
                startTimeMonth,
                startTimeDay,
                startTimeHour,
                startTimeMinute,
                estimatedFinishingTimeYear,
                estimatedFinishingTimeMonth,
                estimatedFinishingTimeDay,
                estimatedFinishingTimeHour,
                estimatedFinishingTimeMinute,
                finishingTimeYear,
                finishingTimeMonth,
                finishingTimeDay,
                finishingTimeHour,
                finishingTimeMinute,
                totalValue,
                details);
        var response = customerServiceGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updateCustomerService(
            @RequestParam(required = true) Integer id,
            @RequestBody CustomerServiceUpdateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = customerServiceUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteCustomerService(@RequestParam(required = true) Integer id) throws AuthenticationException {
        var response = customerServiceDeleteUseCase.execute(id);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}
