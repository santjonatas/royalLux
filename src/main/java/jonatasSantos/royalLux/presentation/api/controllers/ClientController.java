package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.client.ClientCreateUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.client.ClientCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity createClient(@RequestBody ClientCreateUseCaseInputDto body) throws AuthenticationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = clientCreateUseCase.execute(user, body);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class).createClient(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}