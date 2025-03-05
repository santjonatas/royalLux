package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetAllUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserGetUseCase userGetUseCase;

    @Autowired
    private UserGetAllUseCase userGetAllUseCase;

    @Autowired
    private UserUpdateUseCase userUpdateUseCase;

    @Autowired
    private UserDeleteUseCase userDeleteUseCase;

    @GetMapping
    public ResponseEntity getUsers(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String username){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new UserGetUseCaseInputDto(id, username);
        var response = userGetUseCase.execute(input);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = userGetAllUseCase.execute();
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PatchMapping
    public ResponseEntity updateUser(
        @RequestParam(required = true) Integer id,
        @RequestBody UserUpdateUseCaseInputDto body){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = userUpdateUseCase.execute(id, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam(required = true) Integer id){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = userDeleteUseCase.execute(id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(null, null)).withRel("update"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
