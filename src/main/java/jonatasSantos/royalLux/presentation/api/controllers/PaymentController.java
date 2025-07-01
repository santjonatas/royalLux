package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.payment.ManualPaymentCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.ManualPaymentCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
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

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private ManualPaymentCreateUseCase paymentCreateUseCase;

    @Autowired
    private PaymentUpdateUseCase paymentUpdateUseCase;

    @Autowired
    private PaymentGetUseCase paymentGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity createPayment(@RequestBody ManualPaymentCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = paymentCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).createRole(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).updateRole(null, null)).withRel("put"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController.class).deleteRole(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(RoleController.class).getRoles(response.roleId(), null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getPayment(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer customerServiceId,
            @RequestParam(required = false) Integer createdByUserId,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) PaymentMethod method,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String paymentToken,
            @RequestParam(required = false) String paymentUrl,
            @RequestParam(required = false) String payerName,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean ascending) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new PaymentGetUseCaseInputDto(
                id,
                customerServiceId,
                createdByUserId,
                status,
                method,
                description,
                transactionId,
                paymentToken,
                paymentUrl,
                payerName);
        var response = paymentGetUseCase.execute(user, input, page, size, ascending);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).createCustomerService(null)).withRel("post"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).getCustomerService(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).updateCustomerService(null, null)).withRel("patch"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).deleteCustomerService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity updatePayment(
            @RequestParam(required = true) Integer id,
            @RequestBody PaymentUpdateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = paymentUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).createCustomerService(null)).withRel("post"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).getCustomerService(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).updateCustomerService(null, null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerServiceController.class).deleteCustomerService(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }


}
