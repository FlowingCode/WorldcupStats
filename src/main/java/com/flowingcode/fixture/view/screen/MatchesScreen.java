package com.flowingcode.fixture.view.screen;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.presenter.MatchesPresenter;
import com.flowingcode.fixture.view.util.CssStyles;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "matches", layout = MainLayout.class)
public class MatchesScreen extends VerticalLayout {

    private final MatchesPresenter presenter;

    private final MatchUpdater matchUpdater;

    private final DateFilterDialog dateFilterDialog;

    @Autowired
    public MatchesScreen(final MatchesPresenter presenter, final MatchUpdater matchUpdater, final DateFilterDialog dateFilterDialog) {
        this.presenter = presenter;
        this.matchUpdater = matchUpdater;
        this.dateFilterDialog = dateFilterDialog;
        presenter.setView(this);

        // center components
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        presenter.loadResults();
    }

    public void init(final LocalDate date, final List<MatchResultDto> results) {
        this.removeAll();
        final String titleCaption = Optional.ofNullable(date).map(_date -> DateTimeUtil.styleDate(_date) + " matches").orElse("All matches");

        final Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchIcon.addClassName(CssStyles.PADDING_LEFT_10);
        searchIcon.addClassName(CssStyles.CLICKABLE);
        searchIcon.getElement().addEventListener("click", e -> dateFilterDialog.open(presenter::filterByDate));

        this.add(new H3(new Label(titleCaption), searchIcon));

        for (final MatchResultDto result : results) {
            final MatchResultComponent matchResultComponent = new MatchResultComponent(result, matchUpdater);
            this.add(matchResultComponent);
        }
    }

}
