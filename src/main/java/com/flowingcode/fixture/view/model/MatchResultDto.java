package com.flowingcode.fixture.view.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.flowingcode.fixture.view.enums.MatchStatus;

public class MatchResultDto implements MatchResume {

    public String stage;

    public String groupName;

    public String homeTeam;

    public String awayTeam;

    public String homeTeamGoals;

    public String awayTeamGoals;

    public String homeTeamCode;

    public String awayTeamCode;

    public ZonedDateTime kickoff;

    public String minutes;

    public String homeTeamFlag;

    public String awayTeamFlag;

    public MatchStatus status;

    public String fifaId;

    public String stageName;

    @Override
    public String getStage() {
        return stage;
    }

    public void setStage(final String stage) {
        this.stage = stage;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(final String homeTeam) {
        this.homeTeam = homeTeam;
    }

    @Override
    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(final String awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Override
    public String getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public void setHomeTeamGoals(final String homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    @Override
    public String getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public void setAwayTeamGoals(final String awayTeamGoals) {
        this.awayTeamGoals = awayTeamGoals;
    }

    @Override
    public ZonedDateTime getKickoff() {
        return kickoff;
    }

    public void setKickoff(final ZonedDateTime kickoff) {
        this.kickoff = kickoff;
    }

    @Override
    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(final String minutes) {
        this.minutes = minutes;
    }

    @Override
    public String getHomeTeamFlag() {
        return homeTeamFlag;
    }

    public void setHomeTeamFlag(final String homeTeamFlag) {
        this.homeTeamFlag = homeTeamFlag;
    }

    @Override
    public String getAwayTeamFlag() {
        return awayTeamFlag;
    }

    public void setAwayTeamFlag(final String awayTeamFlag) {
        this.awayTeamFlag = awayTeamFlag;
    }

    @Override
    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(final MatchStatus status) {
        this.status = status;
    }

    @Override
    public String getFifaId() {
        return fifaId;
    }

    public void setFifaId(final String fifaId) {
        this.fifaId = fifaId;
    }

    @Override
    public String getHomeTeamCode() {
        return homeTeamCode;
    }

    @Override
    public String getAwayTeamCode() {
        return awayTeamCode;
    }

    public void setHomeTeamCode(final String homeTeamCode) {
        this.homeTeamCode = homeTeamCode;
    }

    public void setAwayTeamCode(final String awayTeamCode) {
        this.awayTeamCode = awayTeamCode;
    }

    @Override
    public String getStageName() {
        return stageName;
    }

    public void setStageName(final String stageName) {
        this.stageName = stageName;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        final MatchResultDto other = (MatchResultDto) obj;
        return this.fifaId.equals(other.fifaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fifaId);
    }
}
