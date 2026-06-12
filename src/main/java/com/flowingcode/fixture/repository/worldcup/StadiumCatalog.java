package com.flowingcode.fixture.repository.worldcup;

import java.time.ZoneId;
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

    /** Fallback when a stadium id is unknown (the bulk of venues are Eastern). */
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("America/New_York");

    /**
     * Host-venue time zone per worldcup26.ir stadium id. The API ships kick-off
     * times as the venue's local wall-clock with no zone/offset, so we attach
     * the real zone here. IANA ids carry the correct DST rules (including
     * Mexico's nationwide DST abolition), so no manual offset handling is needed.
     */
    private static final Map<String, ZoneId> ZONE_BY_STADIUM_ID = Map.ofEntries(
            Map.entry("1", ZoneId.of("America/Mexico_City")),  // Estadio Azteca, Mexico City
            Map.entry("2", ZoneId.of("America/Mexico_City")),  // Estadio Akron, Guadalajara
            Map.entry("3", ZoneId.of("America/Monterrey")),    // Estadio BBVA, Monterrey
            Map.entry("4", ZoneId.of("America/Chicago")),      // AT&T Stadium, Dallas
            Map.entry("5", ZoneId.of("America/Chicago")),      // NRG Stadium, Houston
            Map.entry("6", ZoneId.of("America/Chicago")),      // Arrowhead Stadium, Kansas City
            Map.entry("7", ZoneId.of("America/New_York")),     // Mercedes-Benz Stadium, Atlanta
            Map.entry("8", ZoneId.of("America/New_York")),     // Hard Rock Stadium, Miami
            Map.entry("9", ZoneId.of("America/New_York")),     // Gillette Stadium, Boston
            Map.entry("10", ZoneId.of("America/New_York")),    // Lincoln Financial Field, Philadelphia
            Map.entry("11", ZoneId.of("America/New_York")),    // MetLife Stadium, New York/New Jersey
            Map.entry("12", ZoneId.of("America/Toronto")),     // BMO Field, Toronto
            Map.entry("13", ZoneId.of("America/Vancouver")),   // BC Place, Vancouver
            Map.entry("14", ZoneId.of("America/Los_Angeles")), // Lumen Field, Seattle
            Map.entry("15", ZoneId.of("America/Los_Angeles")), // Levi's Stadium, San Francisco Bay Area
            Map.entry("16", ZoneId.of("America/Los_Angeles"))  // SoFi Stadium, Los Angeles
    );

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

    /** Local time zone of the given venue; falls back to Eastern for unknown/missing ids. */
    public ZoneId zoneById(final String id) {
        return id != null ? ZONE_BY_STADIUM_ID.getOrDefault(id, DEFAULT_ZONE) : DEFAULT_ZONE;
    }

}
