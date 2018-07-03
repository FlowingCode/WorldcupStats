package com.flowingcode.fixture.view.presenter;

import java.time.LocalDate;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.screen.WelcomeScreen;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class WelcomePresenter {

    private final MatchService matchService;

    private WelcomeScreen view;

    public WelcomePresenter(final MatchService matchService) {
        this.matchService = matchService;
    }

    public void setView(final WelcomeScreen view) {
        this.view = view;
    }

    public void loadResults() {
        view.init(matchService.getFutureMatches(LocalDate.now(), LocalDate.of(2018, 7, 31)));
    }

}
