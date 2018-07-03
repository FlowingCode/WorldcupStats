package com.flowingcode.fixture.view.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.addons.applayout.AppLayout;
import com.flowingcode.addons.applayout.menu.MenuItem;
import com.flowingcode.fixture.view.util.MatchUpdater;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.ui.LoadMode;

/**
 * The main view contains a simple label element and a template element.
 */
@SuppressWarnings("serial")
@HtmlImport(value = "styles/shared-styles.html", loadMode = LoadMode.INLINE)
@PageTitle("World Cup 2018 Stats - by Flowing Code S.A.")
@Push
public class MainLayout extends VerticalLayout implements RouterLayout, PageConfigurator {

    @Autowired
    private MatchUpdater matchUpdater;

    public MainLayout() {
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        final AppLayout app = new AppLayout("World Cup 2018 Stats");
        app.setMenuItems(
                new MenuItem("Home", () -> UI.getCurrent().navigate("")),
                new MenuItem("Matches", () -> UI.getCurrent().navigate("matches")),
                new MenuItem("Groups", () -> UI.getCurrent().navigate("groups")),
                new MenuItem("About ...", () -> UI.getCurrent().navigate("about")));

        this.add(app);
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        getUI().ifPresent(ui -> ui.addDetachListener(e -> matchUpdater.unregisterAll(e.getUI())));
    }

    @Override
    public void configurePage(final InitialPageSettings settings) {
        settings.addMetaTag("viewport", "width=device-width, initial-scale=1.0");
        settings.addLink("shortcut icon", "/frontend/images/favicons/favicon-96x96.png");
        settings.addLink("manifest", "/manifest.json");
        settings.addFavIcon("icon", "/frontend/images/favicons/favicon-96x96.png", "96x96");
    }

}
