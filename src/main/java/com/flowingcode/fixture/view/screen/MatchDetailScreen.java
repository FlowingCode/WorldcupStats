package com.flowingcode.fixture.view.screen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.addons.applayout.PaperCard;
import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.model.EventGridDto;
import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.model.TeamEventDto;
import com.flowingcode.fixture.view.presenter.MatchDetailPresenter;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "detail", layout = MainLayout.class)
@PageTitle(value = MainLayout.SITE_TITLE)
public class MatchDetailScreen extends VerticalLayout implements HasUrlParameter<String> {

    private String matchId;

    private final Label date;

    private final Label status;

    private final MatchDetailPresenter presenter;

    private final Grid<EventGridDto> grid;

    private final MatchUpdater matchUpdater;

    private final Column<EventGridDto> awayColumn;

    private final Column<EventGridDto> homeColumn;

    private final PaperCard pc;

    @Autowired
    public MatchDetailScreen(final MatchDetailPresenter presenter, final MatchUpdater matchUpdater) {
        this.matchUpdater = matchUpdater;
        presenter.setView(this);
        this.presenter = presenter;
        final Div content = new Div();
        date = new Label(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        date.setSizeFull();
        status = new Label("Full-time");
        status.setWidth("100px");
        final HorizontalLayout hl = new HorizontalLayout(date, status);
        hl.setSizeFull();

        content.add(hl);

        grid = new Grid<>();

        awayColumn = grid.addColumn(new ComponentRenderer<Div, EventGridDto>(e -> createComponentRenderer(e.getAwayTeamEvent())))
                .setHeader("Away Team");
        homeColumn = grid.addColumn(new ComponentRenderer<Div, EventGridDto>(e -> createComponentRenderer(e.getHomeTeamEvent())))
                .setHeader("Home Team");

        grid.setHeightByRows(true);

        content.add(grid);
        pc = new PaperCard(content);
        pc.addClassName("common-card");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private Div createComponentRenderer(final TeamEventDto event) {
        final Div container = new Div();
        if (event != null) {
            final Div firstLine = new Div();
            firstLine.setText(event.getTime() + " - " + event.getPlayer());
            firstLine.getElement().setAttribute("style", "text-align: center");
            final Div secondLine = new Div();
            secondLine.setText(event.getTypeOfEvent().getDisplay());
            secondLine.getElement().setAttribute("style", "font-weight: bold; text-align: center");
            container.add(firstLine, secondLine);
        } else {
            container.setText("-");
        }
        return container;
    }

    @Override
    public void setParameter(final BeforeEvent event, final String parameter) {
        matchId = parameter;
        presenter.loadResults(matchId);
    }

    public void init(final MatchDetailDto match) {
        final List<EventGridDto> list = new ArrayList<>();
        final MatchDetailDto dto = match;
        final MatchResultComponent mrc = new MatchResultComponent(dto, matchUpdater, false);
        mrc.addClassName("common-card");
        this.add(mrc, pc);
        final H4 awayHeader = new H4(dto.getAwayTeam());
        awayHeader.getElement().setAttribute("style", "text-align: center; margin: 9");
        final H4 homeHeader = new H4(dto.getHomeTeam());
        homeHeader.getElement().setAttribute("style", "text-align: center; margin: 9");
        awayColumn.setHeader(awayHeader);
        homeColumn.setHeader(homeHeader);

        date.setText(dto.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        final List<TeamEventDto> ate = dto.getAwayTeamEvents();
        final List<TeamEventDto> hte = dto.getHomeTeamEvents();
        if (ate.size() > hte.size()) {
            ate.forEach(aevent -> list.add(new EventGridDto(null, aevent)));
            hte.forEach(aevent -> list.get(hte.indexOf(aevent)).setHomeTeamEvent(aevent));
        } else {
            hte.forEach(aevent -> list.add(new EventGridDto(aevent, null)));
            ate.forEach(aevent -> list.get(ate.indexOf(aevent)).setAwayTeamEvent(aevent));
        }
        grid.setItems(list);
    }

    public void goHome() {
        getUI().get().navigate(WelcomeScreen.class);
    }

}
