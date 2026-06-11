package com.flowingcode.fixture.view.component;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;

import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.LiveScore;
import com.flowingcode.fixture.view.model.MatchResume;
import com.flowingcode.fixture.view.screen.CountryScreen;
import com.flowingcode.fixture.view.screen.MatchDetailScreen;
import com.flowingcode.fixture.view.util.CssStyles;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.flowingcode.fixture.view.util.LiveScoreSignals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.signals.shared.SharedValueSignal;

@SuppressWarnings("serial")
public class MatchResultComponent extends Card {

    public static final String WIDTH_50 = "50%";

    public static final String WITDH_FULL = "100%";

    private static final String EMPTY = "";

    private static final String FULL_TIME = "FT";

    private static final String GROUP_LABEL = " GROUP ";

    private static final String SEPARATOR = " - ";

    private static final String DETAILS_BUTTON_CAPTION = "Details";

    final NativeLabel matchDateLeft = new NativeLabel();

    final NativeLabel matchDateRight = new NativeLabel();

    final NativeLabel homeTeamGoals = new NativeLabel();

    final NativeLabel awayTeamGoals = new NativeLabel();

    private final MatchResume matchResume;

    private Span matchTimeContainer;

    private Span dotsContainer;

    public MatchResultComponent(final MatchResume dto, final LiveScoreSignals liveScores) {
        this(dto, liveScores, true);
    }

    public MatchResultComponent(final MatchResume dto, final LiveScoreSignals liveScores, final boolean showDetailsButton) {
        this.matchResume = dto;

        // Bind every live-changing element to this match's shared signal. The
        // bindings register effects that Vaadin tears down automatically on
        // detach — no manual register/unregister, no UI.access().
        final SharedValueSignal<LiveScore> live = liveScores.register(dto);

        addClassName("common-card");
        add(createContent(dto, live));
        if (ZonedDateTime.now().compareTo(dto.getKickoff()) > 0 && showDetailsButton) {
            addToFooter(new Button(DETAILS_BUTTON_CAPTION, e ->
                    UI.getCurrent().navigate(MatchDetailScreen.class, dto.getFifaId())));
        }
    }

    public Div createContent(final MatchResume dto, final SharedValueSignal<LiveScore> live) {
        final Div content = new Div();
        content.setId(DateTimeUtil.styleDate(dto.getKickoff()));

        // header: date & time (both reflect the live status)
        matchDateLeft.addClassName("font-bold");
        matchDateLeft.bindText(live.map(this::matchDateLeftText));
        matchDateRight.addClassName("font-bold");
        matchDateRight.bindText(live.map(this::matchDateRightText));

        final Span dotSpan = new Span(".");
        dotSpan.addClassName("dot-1");
        final Span dotSpan2 = new Span(".");
        dotSpan2.addClassName("dot-2");
        final Span dotSpan3 = new Span(".");
        dotSpan3.addClassName("dot-3");
        dotsContainer = new Span(dotSpan, dotSpan2, dotSpan3);

        matchTimeContainer = new Span(matchDateRight, dotsContainer);

        // The "playing" styling and the jumping dots are now driven reactively
        // by the match status, replacing the imperative class toggling.
        matchTimeContainer.bindClassName(CssStyles.MATCH_PLAYING, live.map(this::isInProgress));
        dotsContainer.bindClassName(CssStyles.JUMPING_DOTS, live.map(this::isInProgress));
        dotsContainer.bindClassName(CssStyles.HIDDEN, live.map(ls -> !isInProgress(ls)));

        final HorizontalLayout headerLayout = new HorizontalLayout(matchDateLeft, matchTimeContainer);
        headerLayout.setFlexGrow(1.0, matchDateLeft);

        final Image homeTeamImage = StringUtils.isNotBlank(dto.getHomeTeamFlag()) ? new Image(dto.getHomeTeamFlag(), dto.getHomeTeam()) : new Image();
        homeTeamImage.addClassName("logo-result-card");
        homeTeamImage.addClassName("group");
        final Anchor homeTeamName = new Anchor("/" + CountryScreen.COUNTRY_ROUTE + "/" + dto.getHomeTeamCode(), dto.getHomeTeam());
        homeTeamGoals.bindText(live.map(LiveScore::homeGoals));
        homeTeamGoals.addClassName("results-numbers-font-style");
        homeTeamGoals.addClassName("text-align-center");
        homeTeamGoals.setWidth("12%");
        final NativeLabel resultSeparator = new NativeLabel(SEPARATOR);
        resultSeparator.addClassName("results-numbers-font-style");
        resultSeparator.addClassName("text-align-center");
        resultSeparator.setWidth("6%");
        final Image awayTeamImage = StringUtils.isNotBlank(dto.getAwayTeamFlag()) ? new Image(dto.getAwayTeamFlag(), dto.getAwayTeam()) : new Image();
        awayTeamImage.addClassName("logo-result-card");
        awayTeamImage.addClassName("group");
        final Anchor awayTeamName = new Anchor("/" + CountryScreen.COUNTRY_ROUTE + "/" + dto.getAwayTeamCode(), dto.getAwayTeam());
        awayTeamGoals.bindText(live.map(LiveScore::awayGoals));
        awayTeamGoals.addClassName("results-numbers-font-style");
        awayTeamGoals.addClassName("text-align-center");
        awayTeamGoals.setWidth("12%");

        // match result
        final VerticalLayout homeTeamLayout = new VerticalLayout(homeTeamImage, homeTeamName);
        homeTeamLayout.getStyle().set("margin", "0px");
        homeTeamLayout.setWidth("35%");
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
        final NativeLabel stage = new NativeLabel(dto.getStage());
        final NativeLabel footerSeparator = new NativeLabel(SEPARATOR);
        final NativeLabel phase;

        if (StringUtils.isNotBlank(dto.getGroupName()) && "First stage".equals(dto.getStageName())) {
            stage.setWidth(WIDTH_50);
            stage.addClassName("text-align-right");
            phase = new NativeLabel(GROUP_LABEL + dto.getGroupName());
            phase.setWidth(WIDTH_50);
            phase.addClassName("text-align-left");
            footerLayout = new HorizontalLayout(stage, footerSeparator, phase);
        } else if (StringUtils.isNotBlank(dto.getStageName())) {
            stage.setWidth(WIDTH_50);
            stage.addClassName("text-align-right");
            phase = new NativeLabel(dto.getStageName());
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

    private boolean isInProgress(final LiveScore live) {
        return live.status() == MatchStatus.IN_PROGRESS;
    }

    private String matchDateLeftText(final LiveScore live) {
        switch (live.status()) {
            case COMPLETED:
            case FUTURE:
                return DateTimeUtil.styleDate(matchResume.getKickoff());
            case IN_PROGRESS:
            case TODAY:
                return MatchStatus.TODAY.name();
            default:
                return EMPTY;
        }
    }

    private String matchDateRightText(final LiveScore live) {
        switch (live.status()) {
            case COMPLETED:
                return FULL_TIME;
            case IN_PROGRESS:
                return live.minutes();
            case TODAY:
            case FUTURE:
                return DateTimeUtil.styleTime(matchResume.getKickoff());
            default:
                return EMPTY;
        }
    }

}
