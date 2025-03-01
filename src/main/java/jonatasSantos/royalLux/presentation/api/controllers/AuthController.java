package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.auth.LoginUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.auth.RegisterUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auth.RegisterUseCaseInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private RegisterUseCase registerUseCase;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUseCaseInputDto body) throws AuthenticationException {
        try {
            var response = loginUseCase.execute(body);
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao realizar login");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUseCaseInputDto body){
        try {
            var response = registerUseCase.execute(body);
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao realizar registro");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
