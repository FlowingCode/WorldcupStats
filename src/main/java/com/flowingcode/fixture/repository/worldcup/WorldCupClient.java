package com.flowingcode.fixture.repository.worldcup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Game;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.GamesResponse;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Group;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.GroupsResponse;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Stadium;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.StadiumsResponse;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Team;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.TeamsResponse;

/**
 * Thin REST client for the free, no-key worldcup26.ir 2026 World Cup API.
 * Failures degrade gracefully to empty lists so the UI never crashes if the
 * third-party service is unavailable.
 */
@Component
public class WorldCupClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldCupClient.class);

    private final RestClient restClient = RestClient.create("https://worldcup26.ir/get");

    public List<Game> getGames() {
        final GamesResponse response = fetch("/games", GamesResponse.class);
        return response != null && response.games() != null ? response.games() : List.of();
    }

    public List<Team> getTeams() {
        final TeamsResponse response = fetch("/teams", TeamsResponse.class);
        return response != null && response.teams() != null ? response.teams() : List.of();
    }

    public List<Group> getGroups() {
        final GroupsResponse response = fetch("/groups", GroupsResponse.class);
        return response != null && response.groups() != null ? response.groups() : List.of();
    }

    public List<Stadium> getStadiums() {
        final StadiumsResponse response = fetch("/stadiums", StadiumsResponse.class);
        return response != null && response.stadiums() != null ? response.stadiums() : List.of();
    }

    private <T> T fetch(final String path, final Class<T> type) {
        try {
            return restClient.get().uri(path).retrieve().body(type);
        } catch (final RuntimeException e) {
            LOGGER.warn("Failed to fetch {} from worldcup26.ir: {}", path, e.getMessage());
            return null;
        }
    }

}
