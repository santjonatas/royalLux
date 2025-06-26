package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.auth.LoginUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.RegisterUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.UserResetPasswordUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.UserSendPasswordRecoveryCodeUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserResetPasswordUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.UserSendPasswordRecoveryCodeUseCaseInputDto;
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
    private UserSendPasswordRecoveryCodeUseCase userSendPasswordRecoveryCodeUseCase;

    @Autowired
    private UserResetPasswordUseCase userResetPasswordUseCase;

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

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody UserResetPasswordUseCaseInputDto body){
        var response = userResetPasswordUseCase.execute(body);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}