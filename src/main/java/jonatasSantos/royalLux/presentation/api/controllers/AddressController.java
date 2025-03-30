package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.AddressStates;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private AddressCreateUseCase addressCreateUseCase;

    @Autowired
    private AddressGetUseCase addressGetUseCase;

    @Autowired
    private AddressUpdateUseCase addressUpdateUseCase;

    @Autowired
    private AddressDeleteUseCase addressDeleteUseCase;

    @PostMapping
    public ResponseEntity createAddress(@RequestBody AddressCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = addressCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(addressCreateUseCase.execute(user, body));

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).createAddress(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).getAddresses(null, null, null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).updateAddress(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).deleteAddress(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AddressController.class).getAddresses(response.addressId(), null, null, null, null, null, null, null, null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getAddresses(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) String complement,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) AddressStates state,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new AddressGetUseCaseInputDto(id, userId, street, houseNumber, complement, neighborhood, city, state, cep);
        var response = addressGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).createAddress(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).getAddresses(null, null, null, null, null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).updateAddress(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).deleteAddress(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    public ResponseEntity updateAddress(
            @RequestParam(required = true) Integer id,
            @RequestBody AddressUpdateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = addressUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).createAddress(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).getAddresses(null, null, null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).updateAddress(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).deleteAddress(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity deleteAddress(@RequestParam(required = true) Integer id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = addressDeleteUseCase.execute(user, id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).createAddress(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).getAddresses(null, null, null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).updateAddress(null, null)).withRel("patch"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).deleteAddress(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}