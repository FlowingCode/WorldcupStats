package com.flowingcode.fixture.view.model;

public class GroupDetailDto {

    private String teamName;

    private String teamLogo;

    private String fifaCode;

    private Integer position;

    private int matchesPlayed;

    private int matchesWon;

    private int matchesDrawn;

    private int matchesLost;

    private int goalsFor;

    private int goalsAgainst;

    private int goalDifference;

    private int points;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(final String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(final String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(final int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(final int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getMatchesDrawn() {
        return matchesDrawn;
    }

    public void setMatchesDrawn(final int matchesDrawn) {
        this.matchesDrawn = matchesDrawn;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(final int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(final int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(final int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(final int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public String getFifaCode() {
        return fifaCode;
    }

    public void setFifaCode(final String fifaCode) {
        this.fifaCode = fifaCode;
    }

}
