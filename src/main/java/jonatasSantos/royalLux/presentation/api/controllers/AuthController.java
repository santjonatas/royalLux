package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.auth.LoginUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.RegisterUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserSendPasswordRecoveryCodeUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserSendPasswordRecoveryCodeUseCaseInputDto;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private RegisterUseCase registerUseCase;

    @Autowired
    UserSendPasswordRecoveryCodeUseCase userSendPasswordRecoveryCodeUseCase;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUseCaseInputDto body) throws AuthenticationException {
        var response = loginUseCase.execute(body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).login(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).register(null)).withRel("post"));

        return ResponseEntity.ok(responsePresenter);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUseCaseInputDto body) throws AuthenticationException {
        var response = registerUseCase.execute(body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).login(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).register(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }

    @PostMapping("/sendPasswordRecoveryCode")
    public ResponseEntity sendPasswordRecoveryCode(@RequestBody UserSendPasswordRecoveryCodeUseCaseInputDto body){
        var response = userSendPasswordRecoveryCodeUseCase.execute(body);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}