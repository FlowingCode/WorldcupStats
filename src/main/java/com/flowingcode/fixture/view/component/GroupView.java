package com.flowingcode.fixture.view.component;

import com.flowingcode.addons.applayout.PaperCard;
import com.flowingcode.fixture.view.model.GroupDetailDto;
import com.flowingcode.fixture.view.model.GroupDto;
import com.flowingcode.fixture.view.screen.CountryScreen;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GroupView extends PaperCard {

    final Label groupName = new Label();

    final VerticalLayout groupContainer = new VerticalLayout();

    public GroupView() {
        addClassName("group");
        addClassName("common-card");
        setPadding(false);

        final Label position = new Label("Pos");
        position.addClassName("stat");
        final Label logo = new Label();
        logo.addClassName("logo");
        final Label team = new Label("Team");
        team.addClassName("team");
        final Label matchesPlayed = new Label("MP");
        matchesPlayed.addClassName("stat");
        final Label won = new Label("W");
        won.addClassNames("stat", "stat-hidden");
        final Label drawn = new Label("D");
        drawn.addClassNames("stat", "stat-hidden");
        final Label lost = new Label("L");
        lost.addClassNames("stat", "stat-hidden");
        final Label goalDifference = new Label("GD");
        goalDifference.addClassNames("stat", "stat-hidden");
        final Label goalsFor = new Label("GF");
        goalsFor.addClassName("stat");
        final Label goalsAgainst = new Label("GA");
        goalsAgainst.addClassName("stat");
        final Label points = new Label("Pts");
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
        setCardContent(cardContent);
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
        final Label positionLabel = new Label(group.getPosition() == null ? "" : group.getPosition() + "");
        positionLabel.addClassName("stat");

        final Image logo = new Image(group.getTeamLogo(), group.getTeamName());
        logo.addClassName("logo");
        final String route = UI.getCurrent().getRouter().getUrl(CountryScreen.class, group.getFifaCode());
        final Anchor team = new Anchor(route, group.getTeamName());
        team.addClassName("team");
        final Label matchesPlayed = new Label(group.getMatchesPlayed() + "");
        matchesPlayed.addClassName("stat");
        final Label matchesWon = new Label(group.getMatchesWon() + "");
        matchesWon.addClassNames("stat", "stat-hidden");
        final Label matchesDrawn = new Label(group.getMatchesDrawn() + "");
        matchesDrawn.addClassNames("stat", "stat-hidden");
        final Label matchesLost = new Label(group.getMatchesLost() + "");
        matchesLost.addClassNames("stat", "stat-hidden");
        final Label goalDifference = new Label(group.getGoalDifference() + "");
        goalDifference.addClassNames("stat", "stat-hidden");
        final Label goalsFor = new Label(group.getGoalsFor() + "");
        goalsFor.addClassName("stat");
        final Label goalsAgainst = new Label(group.getGoalsAgainst() + "");
        goalsAgainst.addClassName("stat");
        final Label points = new Label(group.getPoints() + "");
        points.addClassName("stat");
        final HorizontalLayout detailContainer = new HorizontalLayout(positionLabel, logo, team, matchesPlayed, matchesWon, matchesDrawn, matchesLost,
                goalDifference, goalsFor, goalsAgainst, points);
        detailContainer.addClassName("group-detail-container");
        detailContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return detailContainer;
    }

}
