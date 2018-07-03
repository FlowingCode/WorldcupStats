package com.flowingcode.fixture.view.component;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;

import com.flowingcode.addons.applayout.PaperCard;
import com.flowingcode.addons.applayout.menu.MenuItem;
import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.MatchResume;
import com.flowingcode.fixture.view.screen.CountryScreen;
import com.flowingcode.fixture.view.screen.MatchDetailScreen;
import com.flowingcode.fixture.view.util.CssStyles;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class MatchResultComponent extends PaperCard {

    public static final String WIDTH_50 = "50%";

    public static final String WITDH_FULL = "100%";

    private static final String EMPTY = "";

    private static final String FULL_TIME = "FT";

    private static final String GROUP_LABEL = " GROUP ";

    private static final String SEPARATOR = " - ";

    private static final String DETAILS_BUTTON_CAPTION = "Details";

    final Label matchDateRight = new Label();

    final Label homeTeamGoals = new Label();

    final Label awayTeamGoals = new Label();

    private final MatchResume matchResume;

    private final MatchUpdater matchUpdater;

    private Span matchTimeContainer;

    private Span dotsContainer;

    public MatchResultComponent(final MatchResume dto, final MatchUpdater matchUpdater) {
        this(dto, matchUpdater, true);
    }

    public MatchResultComponent(final MatchResume dto, final MatchUpdater matchUpdater, final boolean showDetailsButton) {
        this.matchResume = dto;
        this.matchUpdater = matchUpdater;

        addClassName("common-card");
        setCardContent(createContent(dto));
        if (ZonedDateTime.now().compareTo(dto.getKickoff()) > 0 && showDetailsButton) {
            setCardActions(new MenuItem(DETAILS_BUTTON_CAPTION, () -> {
                UI.getCurrent().navigate(MatchDetailScreen.class, dto.getFifaId());
            }));
        }
    }

    public Div createContent(final MatchResume dto) {
        final Div content = new Div();
        content.setId(DateTimeUtil.styleDate(dto.getKickoff()));

        // header: date & time
        final Label matchDateLeft = new Label(getMatchDateLeft(dto));
        matchDateLeft.addClassName("font-bold");
        matchDateRight.addClassName("font-bold");

        final Span dotSpan = new Span(".");
        dotSpan.addClassName("dot-1");
        final Span dotSpan2 = new Span(".");
        dotSpan2.addClassName("dot-2");
        final Span dotSpan3 = new Span(".");
        dotSpan3.addClassName("dot-3");
        dotsContainer = new Span(dotSpan, dotSpan2, dotSpan3);
        dotsContainer.addClassName(CssStyles.HIDDEN);

        matchTimeContainer = new Span(matchDateRight, dotsContainer);

        matchDateRight.setText(getMatchDateRight(dto));

        final HorizontalLayout headerLayout = new HorizontalLayout(matchDateLeft, matchTimeContainer);
        headerLayout.setFlexGrow(1.0, matchDateLeft);

        final Image homeTeamImage = StringUtils.isNotBlank(dto.getHomeTeamFlag()) ? new Image(dto.getHomeTeamFlag(), dto.getHomeTeam()) : new Image();
        homeTeamImage.addClassName("logo-result-card");
        homeTeamImage.addClassName("group");
        final Anchor homeTeamName = new Anchor("/" + CountryScreen.COUNTRY_ROUTE + "/" + dto.getHomeTeamCode(), dto.getHomeTeam());
        homeTeamGoals.setText(dto.getHomeTeamGoals());
        homeTeamGoals.addClassName("results-numbers-font-style");
        homeTeamGoals.addClassName("text-align-center");
        homeTeamGoals.setWidth("12%");
        final Label resultSeparator = new Label(SEPARATOR);
        resultSeparator.addClassName("results-numbers-font-style");
        resultSeparator.addClassName("text-align-center");
        resultSeparator.setWidth("6%");
        final Image awayTeamImage = StringUtils.isNotBlank(dto.getAwayTeamFlag()) ? new Image(dto.getAwayTeamFlag(), dto.getAwayTeam()) : new Image();
        awayTeamImage.addClassName("logo-result-card");
        awayTeamImage.addClassName("group");
        final Anchor awayTeamName = new Anchor("/" + CountryScreen.COUNTRY_ROUTE + "/" + dto.getAwayTeamCode(), dto.getAwayTeam());
        awayTeamGoals.setText(dto.getAwayTeamGoals());
        awayTeamGoals.addClassName("results-numbers-font-style");
        awayTeamGoals.addClassName("text-align-center");
        awayTeamGoals.setWidth("12%");

        // match result
        final VerticalLayout homeTeamLayout = new VerticalLayout(homeTeamImage, homeTeamName);
        homeTeamLayout.getElement().setAttribute("style", "margin:0px;width:35%");
        homeTeamLayout.addClassName("align-items-center");
        final VerticalLayout awayTeamLayout = new VerticalLayout(awayTeamImage, awayTeamName);
        awayTeamLayout.setWidth("35%");
        awayTeamLayout.addClassName("align-items-center");

        final HorizontalLayout matchLayout = new HorizontalLayout(homeTeamLayout, homeTeamGoals, resultSeparator, awayTeamGoals, awayTeamLayout);
        matchLayout.addClassName("align-items-center");

        // footer: stage, group, etc
        final HorizontalLayout footerLayout = createFooterLayout(dto);
        footerLayout.setWidth(WITDH_FULL);

        content.add(headerLayout, matchLayout, footerLayout);

        return content;
    }

    private HorizontalLayout createFooterLayout(final MatchResume dto) {
        final HorizontalLayout footerLayout;
        final Label stage = new Label(dto.getStage());
        final Label footerSeparator = new Label(SEPARATOR);
        final Label phase;

        if (StringUtils.isNotBlank(dto.getGroupName()) && "First stage".equals(dto.getStageName())) {
            stage.setWidth(WIDTH_50);
            stage.addClassName("text-align-right");
            phase = new Label(GROUP_LABEL + dto.getGroupName());
            phase.setWidth(WIDTH_50);
            phase.addClassName("text-align-left");
            footerLayout = new HorizontalLayout(stage, footerSeparator, phase);
        } else if (StringUtils.isNotBlank(dto.getStageName())) {
            stage.setWidth(WIDTH_50);
            stage.addClassName("text-align-right");
            phase = new Label(dto.getStageName());
            phase.setWidth(WIDTH_50);
            phase.addClassName("text-align-left");
            footerLayout = new HorizontalLayout(stage, footerSeparator, phase);
        } else {
            stage.setWidth(WITDH_FULL);
            stage.addClassName("text-align-center");
            footerLayout = new HorizontalLayout(stage);
        }
        return footerLayout;
    }

    private String getMatchDateLeft(final MatchResume dto) {
        switch (dto.getStatus()) {
            case COMPLETED:
                return DateTimeUtil.styleDate(dto.getKickoff());
            case IN_PROGRESS:
                return MatchStatus.TODAY.name().toUpperCase();
            case TODAY:
                return MatchStatus.TODAY.name().toUpperCase();
            case FUTURE:
                return DateTimeUtil.styleDate(dto.getKickoff());
            default:
                return EMPTY;
        }
    }

    private String getMatchDateRight(final MatchResume dto) {
        switch (dto.getStatus()) {
            case COMPLETED:
                matchTimeContainer.removeClassName(CssStyles.MATCH_PLAYING);
                dotsContainer.removeClassName(CssStyles.JUMPING_DOTS);
                dotsContainer.addClassName(CssStyles.HIDDEN);
                return FULL_TIME;
            case IN_PROGRESS:
                if (!matchTimeContainer.hasClassName(CssStyles.MATCH_PLAYING)) {
                    dotsContainer.removeClassName(CssStyles.HIDDEN);
                    matchTimeContainer.addClassName(CssStyles.MATCH_PLAYING);
                    dotsContainer.addClassName(CssStyles.JUMPING_DOTS);
                }
                return dto.getMinutes();
            case TODAY:
                dotsContainer.addClassName(CssStyles.HIDDEN);
                matchTimeContainer.removeClassName(CssStyles.MATCH_PLAYING);
                dotsContainer.removeClassName(CssStyles.JUMPING_DOTS);
                return DateTimeUtil.styleTime(dto.getKickoff());
            case FUTURE:
                dotsContainer.addClassName(CssStyles.HIDDEN);
                matchTimeContainer.removeClassName(CssStyles.MATCH_PLAYING);
                dotsContainer.removeClassName(CssStyles.JUMPING_DOTS);
                return DateTimeUtil.styleTime(dto.getKickoff());
            default:
                return EMPTY;
        }
    }

    public void refresh(final MatchResume matchResume) {
        if (this.matchResume.getFifaId().equals(matchResume.getFifaId())) {
            matchDateRight.setText(getMatchDateRight(matchResume));
            homeTeamGoals.setText(matchResume.getHomeTeamGoals());
            awayTeamGoals.setText(matchResume.getAwayTeamGoals());
        }
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        if (!matchResume.getStatus().equals(MatchStatus.COMPLETED)) {
            matchUpdater.registerListener(this);
        }
    }

    @Override
    protected void onDetach(final DetachEvent detachEvent) {
        matchUpdater.unregisterListener(this);
    }

}
