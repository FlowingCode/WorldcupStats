package com.flowingcode.fixture.view.screen;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.presenter.CountryPresenter;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = CountryScreen.COUNTRY_ROUTE, layout = MainLayout.class)
public class CountryScreen extends VerticalLayout implements HasUrlParameter<String> {

    public static final String COUNTRY_ROUTE = "country";

    private final VerticalLayout groupsContainer = new VerticalLayout();

    private final CountryPresenter presenter;

    private final MatchUpdater matchUpdater;

    @Autowired
    public CountryScreen(final CountryPresenter presenter, final MatchUpdater matchUpdater) {
        this.presenter = presenter;
        this.matchUpdater = matchUpdater;

        presenter.setView(this);

        this.add(groupsContainer);

        // center components
        groupsContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private Optional<String> getTeamName(final List<MatchResultDto> groups) {
        final Function<MatchResultDto, Stream<String>> fTeams = dto -> Stream.of(dto.getAwayTeamCode(), dto.getHomeTeamCode());
        final Set<String> teams = groups.stream().flatMap(fTeams).collect(Collectors.toCollection(HashSet::new));
        groups.stream().map(fTeams).map(s -> s.collect(Collectors.toList())).forEach(teams::retainAll);

        if (teams.size() == 1) {
            final String countryCode = teams.iterator().next();
            return Stream.concat(
                    groups.stream().map(dto -> Pair.of(dto.getAwayTeamCode(), dto.getAwayTeam())),
                    groups.stream().map(dto -> Pair.of(dto.getHomeTeamCode(), dto.getHomeTeam()))).filter(e -> e.getLeft().equals(countryCode)).findAny()
                    .map(Pair::getRight);
        } else {
            return Optional.empty();
        }
    }

    public void init(final List<MatchResultDto> groups) {
        groupsContainer.add(new H3(getTeamName(groups).orElse("Country") + " matches"));
        for (final MatchResultDto dto : groups) {
            groupsContainer.add(new MatchResultComponent(dto, matchUpdater));
        }
    }

    @Override
    public void setParameter(final BeforeEvent ev, final String country) {
        presenter.loadResults(country);
    }

}
