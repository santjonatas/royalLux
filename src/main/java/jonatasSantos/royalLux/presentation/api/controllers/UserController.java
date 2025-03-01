package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private UserGetUseCase userGetUseCase;

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Integer id){
        try {
            var response = userGetUseCase.execute(id);
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao buscar user");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
