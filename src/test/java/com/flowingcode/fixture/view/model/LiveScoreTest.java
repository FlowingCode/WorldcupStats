package com.flowingcode.fixture.view.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.flowingcode.fixture.view.enums.MatchStatus;

class LiveScoreTest {

    @Test
    void from_copiesTheLiveChangingFields() {
        final MatchResultDto match = new MatchResultDto();
        match.setHomeTeamGoals("2");
        match.setAwayTeamGoals("1");
        match.setMinutes("67'");
        match.setStatus(MatchStatus.IN_PROGRESS);

        final LiveScore live = LiveScore.from(match);

        assertThat(live.homeGoals()).isEqualTo("2");
        assertThat(live.awayGoals()).isEqualTo("1");
        assertThat(live.minutes()).isEqualTo("67'");
        assertThat(live.status()).isEqualTo(MatchStatus.IN_PROGRESS);
    }

    @Test
    void from_toleratesNullMinutesForNonLiveMatches() {
        final MatchResultDto match = new MatchResultDto();
        match.setHomeTeamGoals("0");
        match.setAwayTeamGoals("0");
        match.setStatus(MatchStatus.FUTURE);
        // minutes left null (only set while IN_PROGRESS)

        final LiveScore live = LiveScore.from(match);

        assertThat(live.minutes()).isNull();
        assertThat(live.status()).isEqualTo(MatchStatus.FUTURE);
    }
}
