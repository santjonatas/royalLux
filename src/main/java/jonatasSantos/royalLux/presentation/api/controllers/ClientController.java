package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientGetUseCase clientGetUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity createClient(@RequestBody ClientCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = clientCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).createClient(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getClients(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new ClientGetUseCaseInputDto(id, userId);
        var response = clientGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        return ResponseEntity.ok(responsePresenter);
    }
}