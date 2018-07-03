package com.flowingcode.fixture.view.util;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.component.AccessUI;
import com.flowingcode.fixture.view.component.MatchResultComponent;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class MatchUpdater {

    private final Executor executor;

    private final MatchService matchService;

    private static Set<MatchResultComponent> listeners = new HashSet<>();

    public MatchUpdater(final MatchService matchService, final Executor executor) {
        this.matchService = matchService;
        this.executor = executor;
    }

    public synchronized void registerListener(final MatchResultComponent listener) {
        listeners.add(listener);
    }

    public synchronized void unregisterListener(final MatchResultComponent listener) {
        listeners.remove(listener);
    }

    @Scheduled(cron = "${cron.match.refresh}")
    public void loadCurrentMatches() {
        synchronized (this) {
            listeners.removeAll(listeners.stream().filter(c -> !c.getUI().isPresent()).collect(Collectors.toList()));
        }
        executor.execute(() -> {
            List<MatchResultDto> matches = matchService.getCurrentMatches();
            if (matches.isEmpty()) {
                matches = matchService.getFutureMatches(LocalDate.now(), LocalDate.now().plusDays(1));
            }
            synchronized (this) {
                for (final MatchResultDto matchResume : matches) {
                    listeners.forEach(l -> AccessUI.on(l, () -> l.refresh(matchResume)));
                }
            }
        });
    }

    public synchronized void unregisterAll(final UI ui) {
        listeners
                .removeAll(listeners.stream().filter(c -> !c.getUI().isPresent() || c.getUI().get().equals(ui)).collect(Collectors.toList()));
    }

}
