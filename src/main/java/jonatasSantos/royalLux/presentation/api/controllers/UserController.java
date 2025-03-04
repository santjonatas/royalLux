package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetAllUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.user.UserUpdateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auth.LoginUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

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

    @GetMapping
    public ResponseEntity getUsers(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String username){
        try {
            var input = new UserGetUseCaseInputDto(id, username);

            var response = userGetUseCase.execute(input);
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao buscar user");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        try {
            var response = userGetAllUseCase.execute();
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao buscar todos users");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PatchMapping
    public ResponseEntity updateUser(
        @RequestParam(required = true) Integer id,
        @RequestBody UserUpdateUseCaseInputDto body){
        try {
            var response = userUpdateUseCase.execute(id, body);
            return ResponseEntity.ok(response);
        }
        catch (Exception exception){
            var errorResponse = new LinkedHashMap<String, String>();
            errorResponse.put("error", "Erro ao atualizar user");
            errorResponse.put("message", exception.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
