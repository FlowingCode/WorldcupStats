package com.flowingcode.fixture.view.model;

import java.time.ZonedDateTime;

import com.flowingcode.fixture.view.enums.MatchStatus;

public interface MatchResume {

    String getStage();

    String getGroupName();

    String getHomeTeam();

    String getAwayTeam();

    String getHomeTeamGoals();

    String getAwayTeamGoals();

    ZonedDateTime getKickoff();

    String getMinutes();

    String getHomeTeamFlag();

    String getAwayTeamFlag();

    MatchStatus getStatus();

    String getFifaId();

    String getHomeTeamCode();

    String getAwayTeamCode();

    String getStageName();

}