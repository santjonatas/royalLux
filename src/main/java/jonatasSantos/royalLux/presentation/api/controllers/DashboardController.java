package jonatasSantos.royalLux.presentation.api.controllers;

import jonatasSantos.royalLux.core.application.contracts.usecases.dashboard.DashboardGetUseCase;
import jonatasSantos.royalLux.core.application.contracts.usecases.material.MaterialGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.dashboard.DashboardGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.material.MaterialGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.DashboardPeriod;
import jonatasSantos.royalLux.presentation.api.presenters.ResponsePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    @Autowired
    private DashboardGetUseCase dashboardGetUseCase;

    @GetMapping
    public ResponseEntity getDashboards(
            @RequestParam(required = true) DashboardPeriod period){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var input = new DashboardGetUseCaseInputDto(period);
        var response = dashboardGetUseCase.execute(user, input);
        var responsePresenter = new ResponsePresenter(response);

        responsePresenter.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DashboardController.class).getDashboards(null)).withSelfRel());

        return ResponseEntity.ok(responsePresenter);
    }
}
