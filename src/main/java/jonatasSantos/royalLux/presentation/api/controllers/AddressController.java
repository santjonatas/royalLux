package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.address.AddressGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
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
import java.util.Date;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private AddressCreateUseCase addressCreateUseCase;

    @Autowired
    private AddressGetUseCase addressGetUseCase;

    @PostMapping
    public ResponseEntity createAddress(@RequestBody AddressCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = addressCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);
//
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withRel("get"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withRel("update"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withRel("delete"));
//
//        URI location = WebMvcLinkBuilder.linkTo(
//                WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(response.personId(), null, null, null, null, null, null, null, null)
//        ).toUri();
//
//        return ResponseEntity.created(location).body(responsePresenter);

        return ResponseEntity.ok(responsePresenter);
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

//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withRel("post"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withRel("update"));
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

}