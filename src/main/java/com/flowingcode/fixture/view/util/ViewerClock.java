package com.flowingcode.fixture.view.util;

import java.time.ZoneId;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Resolves the viewing user's time zone and locale so kick-off times can be
 * shown in the visitor's own local time rather than the venue's.
 *
 * <p>The locale comes from {@link UI#getLocale()} (negotiated from the browser's
 * {@code Accept-Language}). The time zone is only available asynchronously via
 * {@code ExtendedClientDetails} (it needs a client round-trip), so views should
 * render through {@link #whenReady(Runnable)}: the first time it resolves the
 * zone and then runs the action; afterwards it runs synchronously with the
 * cached zone. Scoped per UI, so the round-trip happens once per browser tab.
 */
@SpringComponent
@UIScope
public class ViewerClock {

    private static final ZoneId FALLBACK = ZoneId.of("UTC");

    private ZoneId zone;

    /**
     * Renders now and, the first time, again once the viewer's time zone is
     * known. {@code renderAction} runs immediately (so the view never waits on,
     * or is blocked by, the asynchronous zone lookup); if the zone isn't cached
     * yet it is resolved in the background and {@code renderAction} re-runs once
     * with the resolved zone. After that the zone is cached and a single render
     * is enough.
     */
    // retrieveExtendedClientDetails is soft-deprecated in 25.1 but remains the only
    // API to read the client time zone (UI exposes locale only); the Vaadin docs
    // still document it and there is no replacement yet. Revisit if one ships.
    @SuppressWarnings("deprecation")
    public void render(final Runnable renderAction) {
        renderAction.run();
        if (zone != null) {
            return;
        }
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            zone = parse(details.getTimeZoneId());
            renderAction.run();
        });
    }

    public ZoneId zone() {
        return zone != null ? zone : FALLBACK;
    }

    public Locale locale() {
        final UI ui = UI.getCurrent();
        return ui != null && ui.getLocale() != null ? ui.getLocale() : Locale.getDefault();
    }

    private static ZoneId parse(final String timeZoneId) {
        try {
            return timeZoneId != null ? ZoneId.of(timeZoneId) : FALLBACK;
        } catch (final RuntimeException e) {
            return FALLBACK;
        }
    }

}
