package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.person.PersonCreateUseCase;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
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

    @PostMapping
    public ResponseEntity createPerson(@RequestBody PersonCreateUseCaseInputDto body) throws RoleNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = personCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).createPerson(null)).withSelfRel());
//        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null, null, null)).withRel("get"));

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
            @RequestParam(required = false) String email) throws RoleNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new PersonGetUseCaseInputDto(id, userId, name, dateBirth, cpf, phone, email);
        var response = personGetUseCase.execute(user, input);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    public ResponseEntity updatePerson(
            @RequestParam(required = true) Integer id,
            @RequestBody PersonUpdateUseCaseInputDto body){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = personUpdateUseCase.execute(user, id, body);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}