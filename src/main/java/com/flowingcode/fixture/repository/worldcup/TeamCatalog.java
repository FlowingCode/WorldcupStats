package com.flowingcode.fixture.repository.worldcup;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Team;
import com.flowingcode.fixture.service.FlagUtils;

/**
 * In-memory lookup over the worldcup26.ir teams endpoint, providing the
 * team-id &rarr; FIFA-code &rarr; flag/name/group joins that the games and
 * groups endpoints rely on. Loaded lazily and refreshed on demand; also feeds
 * {@link FlagUtils} so flags resolve by FIFA code anywhere in the app.
 */
@Component
public class TeamCatalog {

    private final WorldCupClient client;

    private volatile Map<String, Team> byId = Map.of();
    private volatile Map<String, Team> byCode = Map.of();

    public TeamCatalog(final WorldCupClient client) {
        this.client = client;
    }

    private synchronized void load() {
        final List<Team> teams = client.getTeams();
        byId = teams.stream().collect(Collectors.toMap(Team::id, Function.identity(), (a, b) -> a));
        byCode = teams.stream().filter(t -> t.fifa_code() != null)
                .collect(Collectors.toMap(Team::fifa_code, Function.identity(), (a, b) -> a));
        FlagUtils.setFlags(teams.stream().filter(t -> t.fifa_code() != null && t.flag() != null)
                .collect(Collectors.toMap(Team::fifa_code, Team::flag, (a, b) -> a)));
    }

    private void ensureLoaded() {
        if (byId.isEmpty()) {
            load();
        }
    }

    public Team byId(final String id) {
        ensureLoaded();
        return byId.get(id);
    }

    public Team byCode(final String fifaCode) {
        ensureLoaded();
        return byCode.get(fifaCode);
    }

    public String codeById(final String id) {
        final Team team = byId(id);
        return team != null ? team.fifa_code() : "";
    }

    public String nameById(final String id) {
        final Team team = byId(id);
        return team != null ? team.name_en() : "";
    }

    public String flagById(final String id) {
        final Team team = byId(id);
        return team != null ? team.flag() : "";
    }

}
