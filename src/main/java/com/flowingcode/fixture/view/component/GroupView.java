package com.flowingcode.fixture.view.component;

import com.flowingcode.fixture.view.model.GroupDetailDto;
import com.flowingcode.fixture.view.model.GroupDto;
import com.flowingcode.fixture.view.screen.CountryScreen;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class GroupView extends Card {

    final NativeLabel groupName = new NativeLabel();

    final VerticalLayout groupContainer = new VerticalLayout();

    public GroupView() {
        addClassName("group");
        addClassName("common-card");

        final NativeLabel position = new NativeLabel("Pos");
        position.addClassName("stat");
        final NativeLabel logo = new NativeLabel();
        logo.addClassName("logo");
        final NativeLabel team = new NativeLabel("Team");
        team.addClassName("team");
        final NativeLabel matchesPlayed = new NativeLabel("MP");
        matchesPlayed.addClassName("stat");
        final NativeLabel won = new NativeLabel("W");
        won.addClassNames("stat", "stat-hidden");
        final NativeLabel drawn = new NativeLabel("D");
        drawn.addClassNames("stat", "stat-hidden");
        final NativeLabel lost = new NativeLabel("L");
        lost.addClassNames("stat", "stat-hidden");
        final NativeLabel goalDifference = new NativeLabel("GD");
        goalDifference.addClassNames("stat", "stat-hidden");
        final NativeLabel goalsFor = new NativeLabel("GF");
        goalsFor.addClassName("stat");
        final NativeLabel goalsAgainst = new NativeLabel("GA");
        goalsAgainst.addClassName("stat");
        final NativeLabel points = new NativeLabel("Pts");
        points.addClassName("stat");

        final HorizontalLayout groupHeader = new HorizontalLayout(position, logo, team, matchesPlayed, won, drawn, lost, goalDifference, goalsFor,
                goalsAgainst,
                points);
        groupHeader.addClassNames("group-header", "group-detail-container");
        groupContainer.add(groupHeader);
        groupContainer.addClassName("group-container");

        groupName.addClassName("group-title");

        groupContainer.setSizeFull();

        final VerticalLayout cardContent = new VerticalLayout(groupName, groupContainer);
        cardContent.setMargin(false);
        cardContent.setPadding(false);
        add(cardContent);
    }

    public void init(final GroupDto group) {
        groupName.setText("Group " + group.getGroupName());

        groupContainer.setSpacing(false);
        groupContainer.setMargin(false);
        groupContainer.setPadding(false);
        for (final GroupDetailDto groupDetail : group.getDetails()) {
            groupContainer.add(this.buildGroupDetail(groupDetail));
        }
    }

    private HorizontalLayout buildGroupDetail(final GroupDetailDto group) {
        final NativeLabel positionLabel = new NativeLabel(group.getPosition() == null ? "" : group.getPosition() + "");
        positionLabel.addClassName("stat");

        final Image logo = new Image(group.getTeamLogo(), group.getTeamName());
        logo.addClassName("logo");
        final String route = "/" + CountryScreen.COUNTRY_ROUTE + "/" + group.getFifaCode();
        final Anchor team = new Anchor(route, group.getTeamName());
        team.addClassName("team");
        final NativeLabel matchesPlayed = new NativeLabel(group.getMatchesPlayed() + "");
        matchesPlayed.addClassName("stat");
        final NativeLabel matchesWon = new NativeLabel(group.getMatchesWon() + "");
        matchesWon.addClassNames("stat", "stat-hidden");
        final NativeLabel matchesDrawn = new NativeLabel(group.getMatchesDrawn() + "");
        matchesDrawn.addClassNames("stat", "stat-hidden");
        final NativeLabel matchesLost = new NativeLabel(group.getMatchesLost() + "");
        matchesLost.addClassNames("stat", "stat-hidden");
        final NativeLabel goalDifference = new NativeLabel(group.getGoalDifference() + "");
        goalDifference.addClassNames("stat", "stat-hidden");
        final NativeLabel goalsFor = new NativeLabel(group.getGoalsFor() + "");
        goalsFor.addClassName("stat");
        final NativeLabel goalsAgainst = new NativeLabel(group.getGoalsAgainst() + "");
        goalsAgainst.addClassName("stat");
        final NativeLabel points = new NativeLabel(group.getPoints() + "");
        points.addClassName("stat");
        final HorizontalLayout detailContainer = new HorizontalLayout(positionLabel, logo, team, matchesPlayed, matchesWon, matchesDrawn, matchesLost,
                goalDifference, goalsFor, goalsAgainst, points);
        detailContainer.addClassName("group-detail-container");
        detailContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return detailContainer;
    }

}
