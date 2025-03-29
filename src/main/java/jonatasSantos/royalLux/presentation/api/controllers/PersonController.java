package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private PersonCreateUseCase personCreateUseCase;

    @Autowired
    private PersonGetUseCase personGetUseCase;

    @Autowired
    private PersonUpdateUseCase personUpdateUseCase;

    @Autowired
    private PersonDeleteUseCase personDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity createPerson(@RequestBody PersonCreateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = personCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getPersons(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date dateBirth,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new PersonGetUseCaseInputDto(id, userId, name, dateBirth, cpf, phone, email);
        var response = personGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    public ResponseEntity updatePerson(
            @RequestParam(required = true) Integer id,
            @RequestBody PersonUpdateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = personUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deletePerson(@RequestParam(required = true) Integer id){
        var response = personDeleteUseCase.execute(id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).getPersons(null, null, null, null, null, null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).updatePerson(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).deletePerson(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}