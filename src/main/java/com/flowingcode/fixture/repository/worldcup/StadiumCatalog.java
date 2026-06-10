package com.flowingcode.fixture.repository.worldcup;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Stadium;

/**
 * In-memory lookup over the worldcup26.ir stadiums endpoint, resolving a
 * stadium id to its venue name and host city.
 */
@Component
public class StadiumCatalog {

    private final WorldCupClient client;

    private volatile Map<String, Stadium> byId = Map.of();

    public StadiumCatalog(final WorldCupClient client) {
        this.client = client;
    }

    private synchronized void load() {
        final List<Stadium> stadiums = client.getStadiums();
        byId = stadiums.stream().collect(Collectors.toMap(Stadium::id, Function.identity(), (a, b) -> a));
    }

    private void ensureLoaded() {
        if (byId.isEmpty()) {
            load();
        }
    }

    public String venueById(final String id) {
        ensureLoaded();
        final Stadium stadium = byId.get(id);
        return stadium != null ? stadium.name_en() : "";
    }

    public String cityById(final String id) {
        ensureLoaded();
        final Stadium stadium = byId.get(id);
        return stadium != null ? stadium.city_en() : "";
    }

}
