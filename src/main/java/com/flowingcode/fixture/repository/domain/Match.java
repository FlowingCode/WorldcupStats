package com.flowingcode.fixture.repository.domain;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Match {
    public final String venue;
    public final String location;
    public final String status;
    public final String time;
    public final String fifa_id;
    public final Weather weather;
    public final String attendance;
    public final List<String> officials;
    public final String stage_name;
    public final Team_statistics home_team_statistics;
    public final Team_statistics away_team_statistics;
    public final ZonedDateTime datetime;
    public final ZonedDateTime last_event_update_at;
    public final ZonedDateTime last_score_update_at;
    public final Team home_team;
    public final Team away_team;
    public final String winner;
    public final String winner_code;
    public final List<Team_event> home_team_events;
    public final List<Team_event> away_team_events;

    @JsonCreator
    public Match(@JsonProperty("venue") final String venue, @JsonProperty("location") final String location, @JsonProperty("status") final String status,
            @JsonProperty("time") final String time, @JsonProperty("fifa_id") final String fifa_id, @JsonProperty("weather") final Weather weather,
            @JsonProperty("attendance") final String attendance, @JsonProperty("officials") final List<String> officials,
            @JsonProperty("stage_name") final String stage_name, @JsonProperty("home_team_statistics") final Team_statistics home_team_statistics,
            @JsonProperty("away_team_statistics") final Team_statistics away_team_statistics, @JsonProperty("datetime") final ZonedDateTime datetime,
            @JsonProperty("last_event_update_at") final ZonedDateTime last_event_update_at,
            @JsonProperty("last_score_update_at") final ZonedDateTime last_score_update_at, @JsonProperty("home_team") final Team home_team,
            @JsonProperty("away_team") final Team away_team, @JsonProperty("winner") final String winner,
            @JsonProperty("winner_code") final String winner_code, @JsonProperty("home_team_events") final List<Team_event> home_team_events,
            @JsonProperty("away_team_events") final List<Team_event> away_team_events) {
        this.venue = venue;
        this.location = location;
        this.status = status;
        this.time = time;
        this.fifa_id = fifa_id;
        this.weather = weather;
        this.attendance = attendance;
        this.officials = officials;
        this.stage_name = stage_name;
        this.home_team_statistics = home_team_statistics;
        this.away_team_statistics = away_team_statistics;
        this.datetime = datetime;
        this.last_event_update_at = last_event_update_at;
        this.last_score_update_at = last_score_update_at;
        this.home_team = home_team;
        this.away_team = away_team;
        this.winner = winner;
        this.winner_code = winner_code;
        this.home_team_events = home_team_events;
        this.away_team_events = away_team_events;
    }

    public static final class Weather {
        public final String humidity;
        public final String temp_celsius;
        public final String temp_farenheit;
        public final String wind_speed;
        public final String description;

        @JsonCreator
        public Weather(@JsonProperty("humidity") final String humidity, @JsonProperty("temp_celsius") final String temp_celsius,
                @JsonProperty("temp_farenheit") final String temp_farenheit, @JsonProperty("wind_speed") final String wind_speed,
                @JsonProperty("description") final String description) {
            this.humidity = humidity;
            this.temp_celsius = temp_celsius;
            this.temp_farenheit = temp_farenheit;
            this.wind_speed = wind_speed;
            this.description = description;
        }
    }

    public static final class Team_statistics {
        public final int attempts_on_goal;
        public final int on_target;
        public final int off_target;
        public final int blocked;
        public final int woodwork;
        public final int corners;
        public final int offsides;
        public final int ball_possession;
        public final int pass_accuracy;
        public final int num_passes;
        public final int passes_completed;
        public final int distance_covered;
        public final int balls_recovered;
        public final int tackles;
        public final int clearances;
        public final int yellow_cards;
        public final int red_cards;
        public final int fouls_committed;
        public final String country;
        public final String tactics;
        public final List<Player> starting_eleven;
        public final List<Player> substitutes;

        @JsonCreator
        public Team_statistics(@JsonProperty("attempts_on_goal") final int attempts_on_goal, @JsonProperty("on_target") final int on_target,
                @JsonProperty("off_target") final int off_target, @JsonProperty("blocked") final int blocked, @JsonProperty("woodwork") final int woodwork,
                @JsonProperty("corners") final int corners, @JsonProperty("offsides") final int offsides,
                @JsonProperty("ball_possession") final int ball_possession, @JsonProperty("pass_accuracy") final int pass_accuracy,
                @JsonProperty("num_passes") final int num_passes, @JsonProperty("passes_completed") final int passes_completed,
                @JsonProperty("distance_covered") final int distance_covered, @JsonProperty("balls_recovered") final int balls_recovered,
                @JsonProperty("tackles") final int tackles, @JsonProperty("clearances") final int clearances,
                @JsonProperty("yellow_cards") final int yellow_cards, @JsonProperty("red_cards") final int red_cards,
                @JsonProperty("fouls_committed") final int fouls_committed, @JsonProperty("country") final String country,
                @JsonProperty("tactics") final String tactics, @JsonProperty("starting_eleven") final List<Player> starting_eleven,
                @JsonProperty("substitutes") final List<Player> substitutes) {
            this.attempts_on_goal = attempts_on_goal;
            this.on_target = on_target;
            this.off_target = off_target;
            this.blocked = blocked;
            this.woodwork = woodwork;
            this.corners = corners;
            this.offsides = offsides;
            this.ball_possession = ball_possession;
            this.pass_accuracy = pass_accuracy;
            this.num_passes = num_passes;
            this.passes_completed = passes_completed;
            this.distance_covered = distance_covered;
            this.balls_recovered = balls_recovered;
            this.tackles = tackles;
            this.clearances = clearances;
            this.yellow_cards = yellow_cards;
            this.red_cards = red_cards;
            this.fouls_committed = fouls_committed;
            this.country = country;
            this.tactics = tactics;
            this.starting_eleven = starting_eleven;
            this.substitutes = substitutes;
        }

        public static final class Player {
            public final String name;
            public final boolean captain;
            public final int shirt_number;
            public final String position;

            @JsonCreator
            public Player(@JsonProperty("name") final String name, @JsonProperty("captain") final boolean captain,
                    @JsonProperty("shirt_number") final int shirt_number, @JsonProperty("position") final String position) {
                this.name = name;
                this.captain = captain;
                this.shirt_number = shirt_number;
                this.position = position;
            }
        }

    }

    public static final class Team {
        public final String country;
        public final String code;
        public final int goals;
        public final int penalties;
        public final String team_tbd;

        @JsonCreator
        public Team(@JsonProperty("country") final String country, @JsonProperty("code") final String code, @JsonProperty("goals") final int goals,
                @JsonProperty("penalties") final int penalties, @JsonProperty("team_tbd") final String team_tbd) {
            this.country = country;
            this.code = code;
            this.goals = goals;
            this.penalties = penalties;
            this.team_tbd = team_tbd;
        }
    }

    public static final class Team_event {
        public final int id;
        public final TeamEventType type_of_event;
        public final String player;
        public final String time;

        @JsonCreator
        public Team_event(@JsonProperty("id") final int id, @JsonProperty("type_of_event") final TeamEventType type_of_event,
                @JsonProperty("player") final String player, @JsonProperty("time") final String time) {
            this.id = id;
            this.type_of_event = type_of_event;
            this.player = player;
            this.time = time;
        }
    }

}