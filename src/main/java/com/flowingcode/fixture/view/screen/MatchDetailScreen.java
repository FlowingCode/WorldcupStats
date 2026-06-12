package com.flowingcode.fixture.view.screen;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.model.TeamEventDto;
import com.flowingcode.fixture.view.presenter.MatchDetailPresenter;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.flowingcode.fixture.view.util.LiveScoreSignals;
import com.flowingcode.fixture.view.util.ViewerClock;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "detail", layout = MainLayout.class)
@PageTitle(value = MainLayout.SITE_TITLE)
public class MatchDetailScreen extends VerticalLayout implements HasUrlParameter<String> {

    private final MatchDetailPresenter presenter;

    private final LiveScoreSignals liveScores;

    private final ViewerClock clock;

    @Autowired
    public MatchDetailScreen(final MatchDetailPresenter presenter, final LiveScoreSignals liveScores, final ViewerClock clock) {
        this.liveScores = liveScores;
        this.clock = clock;
        this.presenter = presenter;
        presenter.setView(this);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void setParameter(final BeforeEvent event, final String parameter) {
        // Render now; re-render once the viewer's time zone resolves (local times).
        clock.render(() -> presenter.loadResults(parameter));
    }

    public void init(final MatchDetailDto dto) {
        removeAll();

        // Score card (teams, flags, score, kickoff, stage/group) — reused from the match list.
        final MatchResultComponent score = new MatchResultComponent(dto, liveScores, clock, false);
        score.addClassName("common-card");
        add(score);

        final Card detail = new Card();
        detail.addClassName("common-card");
        final VerticalLayout body = new VerticalLayout();
        body.setSpacing(true);

        if (StringUtils.isNotBlank(dto.getVenue())) {
            final String place = dto.getVenue() + (StringUtils.isNotBlank(dto.getLocation()) ? ", " + dto.getLocation() : "");
            body.add(infoLine(VaadinIcon.MAP_MARKER, place));
        }
        if (dto.getDateTime() != null) {
            body.add(infoLine(VaadinIcon.CALENDAR_CLOCK,
                    DateTimeUtil.dateTime(dto.getDateTime(), clock.zone(), clock.locale())));
        }

        final List<TeamEventDto> homeGoals = dto.getHomeTeamEvents();
        final List<TeamEventDto> awayGoals = dto.getAwayTeamEvents();
        if (!homeGoals.isEmpty() || !awayGoals.isEmpty()) {
            final H4 goalsTitle = new H4("Goals");
            goalsTitle.getStyle().set("margin", "0");
            body.add(goalsTitle);

            final HorizontalLayout columns = new HorizontalLayout(
                    teamGoals(dto.getHomeTeam(), homeGoals),
                    teamGoals(dto.getAwayTeam(), awayGoals));
            columns.setWidthFull();
            body.add(columns);
        }

        detail.add(body);
        add(detail);
    }

    private Component infoLine(final VaadinIcon icon, final String text) {
        final Icon vaadinIcon = icon.create();
        vaadinIcon.setSize("18px");
        final HorizontalLayout line = new HorizontalLayout(vaadinIcon, new Span(text));
        line.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return line;
    }

    private Component teamGoals(final String teamName, final List<TeamEventDto> goals) {
        final VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setSpacing(false);
        column.setWidthFull();

        final Span header = new Span(teamName);
        header.addClassName("font-bold");
        column.add(header);

        if (goals.isEmpty()) {
            final Span none = new Span("—");
            none.getStyle().set("color", "var(--lumo-secondary-text-color)");
            column.add(none);
        } else {
            for (final TeamEventDto goal : goals) {
                final String minute = StringUtils.isNotBlank(goal.getTime()) ? goal.getTime() + "' " : "";
                column.add(new Span("⚽ " + minute + goal.getPlayer()));
            }
        }
        return column;
    }

    public void goHome() {
        getUI().ifPresent(ui -> ui.navigate(WelcomeScreen.class));
    }

}
