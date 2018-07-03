package com.flowingcode.fixture.view.presenter;

import java.time.LocalDate;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.screen.MatchesScreen;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class MatchesPresenter {

    private final MatchService matchService;

    private MatchesScreen view;

    public MatchesPresenter(final MatchService matchService) {
        this.matchService = matchService;
    }

    public void setView(final MatchesScreen view) {
        this.view = view;
    }

    public void loadResults() {
        view.init(null, matchService.getMatches());
    }

    public void filterByDate(LocalDate date) {
    	view.init(date, matchService.getMatchesByDate(date));		
	}
}
