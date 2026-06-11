package com.flowingcode.fixture.view.screen;

import com.flowingcode.addons.applayout.AppLayout;
import com.flowingcode.addons.applayout.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;

/**
 * The main application layout: a Flowing Code AppLayout (which is itself a
 * {@code RouterLayout}) hosting the navigation menu. Routed views are rendered
 * inside it.
 */
@SuppressWarnings("serial")
@PageTitle(value = MainLayout.SITE_TITLE)
public class MainLayout extends AppLayout {

    public static final String SITE_TITLE = "Global Football 2026 Stats - Flowing Code S.A.";

    public MainLayout() {
        super("Global Football 2026 Stats");
        setMenuItems(
                new MenuItem("Home", () -> UI.getCurrent().navigate("")),
                new MenuItem("Matches", () -> UI.getCurrent().navigate("matches")),
                new MenuItem("Groups", () -> UI.getCurrent().navigate("groups")),
                new MenuItem("About ...", () -> UI.getCurrent().navigate("about")));
    }

}
