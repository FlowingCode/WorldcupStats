package com.flowingcode.fixture.view.screen;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "about", layout = MainLayout.class)
@PageTitle(value = MainLayout.SITE_TITLE)
public class AboutScreen extends VerticalLayout {

    public AboutScreen() {
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        final VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vl.add(new H4("Global Football 2026 Stats - Vaadin 25 Demo Application"));

        final int year = 2026;
        final String html = "<div class=\"about-content\">"
                + "<p>This is a demo application showcasing:</p>"
                + "<ul>"
                + "<li>Vaadin 25. Learn more in <a href=\"https://vaadin.com/docs\" target=\"_blank\">Vaadin's official site</a>.</li>"
                + "<li>Spring Boot. Find out more <a href=\"https://spring.io/\" target=\"_blank\">here</a>.</li>"
                + "<li>Flowing Code's <a href=\"https://vaadin.com/directory/component/app-layout-addon\" target=\"_blank\">App Layout Add-on</a>.</li>"
                + "</ul>"
                + "<p>It shows the fixture and results of the 2026 international football tournament "
                + "(Canada, USA &amp; Mexico), with data from "
                + "<a href=\"https://worldcup26.ir/\" target=\"_blank\">worldcup26.ir</a>.</p>"
                + "<p><small>Unofficial demo. Not affiliated with, endorsed by, or sponsored by FIFA or any "
                + "football governing body. All team and tournament data comes from the public worldcup26.ir API.</small></p>"
                + "<p>Developed by <a href=\"https://www.flowingcode.com\" target=\"_blank\">Flowing Code S.A.</a> &copy; " + year + "</p>"
                + "</div>";
        vl.add(new Html(html));

        final Card card = new Card();
        card.addClassName("common-card");
        card.add(vl);
        this.add(card);
    }

}
