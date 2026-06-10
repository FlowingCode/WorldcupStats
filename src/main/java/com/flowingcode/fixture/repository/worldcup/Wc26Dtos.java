package com.flowingcode.fixture.repository.worldcup;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Records mapping the JSON returned by the worldcup26.ir API
 * (https://worldcup26.ir/get/...). All numeric fields arrive as strings.
 */
public final class Wc26Dtos {

    private Wc26Dtos() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Team(String id, String name_en, String fifa_code, String iso2, String flag, String groups) {
    }

    public record TeamsResponse(List<Team> teams) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Game(String id, String home_team_id, String away_team_id, String home_score, String away_score,
            String home_scorers, String away_scorers, String group, String matchday, String local_date,
            String stadium_id, String finished, String time_elapsed, String type, String home_team_name_en,
            String away_team_name_en) {
    }

    public record GamesResponse(List<Game> games) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GroupTeam(String team_id, String mp, String w, String l, String d, String pts, String gf, String ga,
            String gd) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Group(String name, List<GroupTeam> teams) {
    }

    public record GroupsResponse(List<Group> groups) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Stadium(String id, String name_en, String city_en) {
    }

    public record StadiumsResponse(List<Stadium> stadiums) {
    }
}
