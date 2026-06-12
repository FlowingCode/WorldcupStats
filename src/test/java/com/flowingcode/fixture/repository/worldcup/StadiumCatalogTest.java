package com.flowingcode.fixture.repository.worldcup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StadiumCatalogTest {

    private StadiumCatalog catalog;

    @BeforeEach
    void setUp() {
        // zoneById is a static lookup; the client is never hit.
        catalog = new StadiumCatalog(mock(WorldCupClient.class));
    }

    @Test
    void zoneById_resolvesEachHostVenueToItsZone() {
        assertThat(catalog.zoneById("1")).isEqualTo(ZoneId.of("America/Mexico_City"));  // Estadio Azteca
        assertThat(catalog.zoneById("16")).isEqualTo(ZoneId.of("America/Los_Angeles")); // SoFi, Los Angeles
        assertThat(catalog.zoneById("12")).isEqualTo(ZoneId.of("America/Toronto"));      // BMO Field, Toronto
        assertThat(catalog.zoneById("4")).isEqualTo(ZoneId.of("America/Chicago"));       // AT&T Stadium, Dallas
        assertThat(catalog.zoneById("11")).isEqualTo(ZoneId.of("America/New_York"));     // MetLife, NY/NJ
    }

    @Test
    void zoneById_fallsBackToEasternForUnknownId() {
        assertThat(catalog.zoneById("999")).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(catalog.zoneById(null)).isEqualTo(ZoneId.of("America/New_York"));
    }
}
