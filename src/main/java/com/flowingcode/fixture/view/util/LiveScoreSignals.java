package com.flowingcode.fixture.view.util;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.scheduling.annotation.Scheduled;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.model.LiveScore;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.model.MatchResume;
import com.vaadin.flow.signals.shared.SharedMapSignal;
import com.vaadin.flow.signals.shared.SharedValueSignal;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Application-scoped holder of the live match scores, exposed as shared signals.
 *
 * <p>This replaces the old listener-based {@code MatchUpdater}. A single
 * scheduled task polls the data source and writes the latest scores into a
 * {@link SharedMapSignal} keyed by match id. Components bind to the per-match
 * entry signal (see {@code MatchResultComponent}); when an entry changes, every
 * UI showing that match updates automatically. Shared signals are thread-safe
 * and push their changes out on their own, so there is no manual listener set
 * and no {@code UI.access()} — Vaadin Push ({@code @Push} on the app shell) is
 * the only prerequisite.
 */
@SpringComponent
public class LiveScoreSignals {

    /** One entry per match (key = fifaId); each value is an independently reactive signal. */
    private final SharedMapSignal<LiveScore> liveScores = new SharedMapSignal<>(LiveScore.class);

    private final MatchService matchService;

    private final Executor executor;

    public LiveScoreSignals(final MatchService matchService, final Executor executor) {
        this.matchService = matchService;
        this.executor = executor;
    }

    /**
     * Returns the shared signal carrying live updates for the given match,
     * seeding it with the match's current values the first time it is requested.
     * A later (fresher) value written by {@link #refreshLiveScores()} is kept.
     */
    public SharedValueSignal<LiveScore> register(final MatchResume match) {
        final String fifaId = match.getFifaId();
        liveScores.putIfAbsent(fifaId, LiveScore.from(match));
        return liveScores.peek().get(fifaId);
    }

    @Scheduled(cron = "${cron.match.refresh}")
    public void refreshLiveScores() {
        // Offload the blocking HTTP call so it never stalls the scheduler thread.
        executor.execute(() -> {
            List<MatchResultDto> matches = matchService.getCurrentMatches();
            if (matches.isEmpty()) {
                matches = matchService.getFutureMatches(LocalDate.now(), LocalDate.now().plusDays(1));
            }
            for (final MatchResultDto match : matches) {
                // Atomic, thread-safe; UI updates are pushed automatically.
                liveScores.put(match.getFifaId(), LiveScore.from(match));
            }
        });
    }

}
