package com.flowingcode.fixture.repository.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class GroupWrapper {
    public final Group group;

    @JsonCreator
    public GroupWrapper(@JsonProperty("group") final Group group){
        this.group = group;
    }

    public static final class Group {
        public final int id;
        public final String letter;
        public final List<TeamWrapper.Team> ordered_teams;

        @JsonCreator
        public Group(@JsonProperty("id") final int id, @JsonProperty("letter") final String letter, @JsonProperty("ordered_teams") final List<TeamWrapper.Team> teams) {
            this.id = id;
            this.letter = letter;
            this.ordered_teams = teams;
        }

        public static final class TeamWrapper {
            public final Team team;

            @JsonCreator
            public TeamWrapper(@JsonProperty("team") final Team team){
                this.team = team;
            }

            public static final class Team {
                public final int id;
                public final String country;
                public final String fifa_code;
                public final int points;
                public final int wins;
                public final int draws;
                public final int losses;
                public final int games_played;
                public final int goals_for;
                public final int goals_against;
                public final int goal_differential;

                @JsonCreator
                public Team(@JsonProperty("id") final int id, @JsonProperty("country") final String country, @JsonProperty("fifa_code") final String fifa_code,
                        @JsonProperty("points") final int points, @JsonProperty("wins") final int wins, @JsonProperty("draws") final int draws,
                        @JsonProperty("losses") final int losses, @JsonProperty("games_played") final int games_played,
                        @JsonProperty("goals_for") final int goals_for, @JsonProperty("goals_against") final int goals_against,
                        @JsonProperty("goal_differential") final int goal_differential) {
                    this.id = id;
                    this.country = country;
                    this.fifa_code = fifa_code;
                    this.points = points;
                    this.wins = wins;
                    this.draws = draws;
                    this.losses = losses;
                    this.games_played = games_played;
                    this.goals_for = goals_for;
                    this.goals_against = goals_against;
                    this.goal_differential = goal_differential;
                }
            }
        }
    }
}
