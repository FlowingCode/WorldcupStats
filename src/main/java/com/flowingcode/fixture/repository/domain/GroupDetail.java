package com.flowingcode.fixture.repository.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GroupDetail {

    String getGroup();

    Integer getRank();

    String getTeam();

    Integer getTeamId();

    Integer getPlayedGames();

    String getCrestURI();

    Integer getPoints();

    Integer getGoals();

    Integer getGoalsAgainst();

    Integer getGoalDifference();

}