package com.flowingcode.fixture.view.screen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.component.TitleComponent;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.presenter.WelcomePresenter;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "", layout = MainLayout.class)
public class WelcomeScreen extends VerticalLayout {

    private final WelcomePresenter presenter;

    private final MatchUpdater matchUpdater;

    @Autowired
    public WelcomeScreen(final WelcomePresenter presenter, final MatchUpdater matchUpdater) {
        this.presenter = presenter;
        this.matchUpdater = matchUpdater;
        presenter.setView(this);

        // center components
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        presenter.loadResults();
    }

    public void init(final List<MatchResultDto> results) {
    	this.add(new TitleComponent("Upcoming matches"));
        for (final MatchResultDto result : results) {
            final MatchResultComponent matchResultComponent = new MatchResultComponent(result, matchUpdater);
            this.add(matchResultComponent);
        }
    }

}
