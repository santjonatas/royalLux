package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private PersonCreateUseCase personCreateUseCase;

    @PostMapping
    public ResponseEntity createPerson(@RequestBody PersonCreateUseCaseInputDto body){
        var response = personCreateUseCase.execute(body);

        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null, null, null)).withRel("get"));

        return ResponseEntity.ok(responsePresenter);
    }
}
