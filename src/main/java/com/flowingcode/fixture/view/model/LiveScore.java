package com.flowingcode.fixture.view.model;

import com.flowingcode.fixture.view.enums.MatchStatus;

/**
 * Immutable snapshot of the only parts of a match that change while it is being
 * played: the running score, the elapsed minutes and the status. Carried by the
 * shared signals in {@code LiveScoreSignals} so that a single background refresh
 * propagates to every {@code MatchResultComponent} showing that match, with no
 * manual listeners or {@code UI.access()}.
 */
public record LiveScore(String homeGoals, String awayGoals, String minutes, MatchStatus status) {

    public static LiveScore from(final MatchResume match) {
        return new LiveScore(match.getHomeTeamGoals(), match.getAwayTeamGoals(), match.getMinutes(), match.getStatus());
    }
}
