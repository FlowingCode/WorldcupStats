package com.flowingcode.fixture.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flowingcode.fixture.repository.worldcup.StadiumCatalog;
import com.flowingcode.fixture.repository.worldcup.TeamCatalog;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Game;
import com.flowingcode.fixture.repository.worldcup.WorldCupClient;
import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.MatchResultDto;

/**
 * Exercises the worldcup26.ir &rarr; view-DTO mapping in {@link MatchServiceImpl}:
 * match status derivation and the data-driven stage labels. The remote client
 * and catalogs are mocked, so these are pure mapping tests.
 */
class MatchServiceImplTest {

    private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    private WorldCupClient client;
    private MatchServiceImpl service;

    @BeforeEach
    void setUp() {
        client = mock(WorldCupClient.class);
        service = new MatchServiceImpl(client, mock(TeamCatalog.class), mock(StadiumCatalog.class));
    }

    /** Builds a Game with sensible defaults; override only what a test cares about. */
    private static Game game(final String finished, final String timeElapsed, final String type,
            final String matchday, final String localDate, final String homeScore, final String awayScore) {
        return new Game("1", "10", "20", homeScore, awayScore, null, null, "A", matchday, localDate,
                "100", finished, timeElapsed, type, "Mexico", "Canada");
    }

    private static String today() {
        return LocalDate.now().atTime(20, 0).format(LOCAL_DATE);
    }

    private MatchResultDto convertSingle(final Game g) {
        when(client.getGames()).thenReturn(List.of(g));
        final List<MatchResultDto> result = service.getMatches();
        assertThat(result).hasSize(1);
        return result.get(0);
    }

    @Test
    void status_finishedGameIsCompleted() {
        final MatchResultDto dto = convertSingle(game("TRUE", "90", "group", "1", today(), "2", "1"));
        assertThat(dto.getStatus()).isEqualTo(MatchStatus.COMPLETED);
    }

    @Test
    void status_runningGameIsInProgressAndKeepsElapsedMinutes() {
        final MatchResultDto dto = convertSingle(game("FALSE", "67'", "group", "1", today(), "1", "0"));
        assertThat(dto.getStatus()).isEqualTo(MatchStatus.IN_PROGRESS);
        assertThat(dto.getMinutes()).isEqualTo("67'");
    }

    @Test
    void status_notStartedTodayIsToday() {
        final MatchResultDto dto = convertSingle(game("FALSE", "notstarted", "group", "1", today(), "0", "0"));
        assertThat(dto.getStatus()).isEqualTo(MatchStatus.TODAY);
    }

    @Test
    void status_notStartedFutureDateIsFuture() {
        final String future = LocalDate.now().plusDays(5).atTime(20, 0).format(LOCAL_DATE);
        final MatchResultDto dto = convertSingle(game("FALSE", "notstarted", "group", "2", future, "0", "0"));
        assertThat(dto.getStatus()).isEqualTo(MatchStatus.FUTURE);
    }

    @Test
    void goals_nullOrLiteralNullBecomeZero() {
        final MatchResultDto dto = convertSingle(game("FALSE", "notstarted", "group", "1", today(), null, "null"));
        assertThat(dto.getHomeTeamGoals()).isEqualTo("0");
        assertThat(dto.getAwayTeamGoals()).isEqualTo("0");
    }

    @Test
    void stage_groupGameUsesFirstStageAndMatchdayLabel() {
        final MatchResultDto dto = convertSingle(game("FALSE", "notstarted", "group", "3", today(), "0", "0"));
        assertThat(dto.getStageName()).isEqualTo("First stage");
        assertThat(dto.getStage()).isEqualTo("Matchday 3");
    }

    @Test
    void stage_nullTypeIsTreatedAsGroup() {
        final MatchResultDto dto = convertSingle(game("FALSE", "notstarted", null, "1", today(), "0", "0"));
        assertThat(dto.getStageName()).isEqualTo("First stage");
    }

    @Test
    void stage_knockoutTypesAreHumanized() {
        assertThat(convertSingle(game("FALSE", "notstarted", "round_of_16", null, today(), "0", "0")).getStageName())
                .isEqualTo("Round of 16");
        assertThat(convertSingle(game("FALSE", "notstarted", "quarterfinal", null, today(), "0", "0")).getStageName())
                .isEqualTo("Quarter-finals");
        assertThat(convertSingle(game("FALSE", "notstarted", "semifinal", null, today(), "0", "0")).getStageName())
                .isEqualTo("Semi-finals");
        assertThat(convertSingle(game("FALSE", "notstarted", "thirdPlace", null, today(), "0", "0")).getStageName())
                .isEqualTo("Third place");
        assertThat(convertSingle(game("FALSE", "notstarted", "final", null, today(), "0", "0")).getStageName())
                .isEqualTo("Final");
    }

    @Test
    void getCurrentMatches_returnsOnlyInProgressGames() {
        when(client.getGames()).thenReturn(List.of(
                game("FALSE", "67'", "group", "1", today(), "1", "0"),       // in progress
                game("TRUE", "90", "group", "1", today(), "2", "1"),         // finished
                game("FALSE", "notstarted", "group", "1", today(), "0", "0") // not started
        ));
        assertThat(service.getCurrentMatches()).hasSize(1);
    }
}
