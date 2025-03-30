package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientCreateUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientDeleteUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientGetUseCaseInputDto;
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
import java.net.URI;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientGetUseCase clientGetUseCase;

    @Autowired
    private ClientDeleteUseCase clientDeleteUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity createClient(@RequestBody ClientCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = clientCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).createClient(null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).getClients(null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).deleteClient(null)).withRel("delete"));

        URI location = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ClientController.class).getClients(response.clientId(), null, null, null)
        ).toUri();

        return ResponseEntity.created(location).body(responsePresenter);
    }

    @GetMapping
    public ResponseEntity getClients(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new ClientGetUseCaseInputDto(id, userId);
        var response = clientGetUseCase.execute(user, input, page, size);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).createClient(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).getClients(null, null, null, null)).withSelfRel());
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).deleteClient(null)).withRel("delete"));

        return ResponseEntity.ok(responsePresenter);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteClient(@RequestParam(required = true) Integer id) throws AuthenticationException {
        var response = clientDeleteUseCase.execute(id);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).createClient(null)).withRel("post"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).getClients(null, null, null, null)).withRel("get"));
        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).deleteClient(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}