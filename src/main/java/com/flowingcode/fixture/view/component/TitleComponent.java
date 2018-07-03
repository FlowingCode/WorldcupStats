package com.flowingcode.fixture.view.component;

import com.flowingcode.addons.applayout.PaperCard;
import com.flowingcode.fixture.view.util.CssStyles;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.SerializableRunnable;

@SuppressWarnings("serial")
public class TitleComponent extends PaperCard {

    private final SerializableRunnable searchListener;

    public TitleComponent(final String description) {
        this(description, null);
    }

    public TitleComponent(final String description, final SerializableRunnable searchListener) {
        this.searchListener = searchListener;

        addClassName(CssStyles.COMMON_CARD);
        final H3 title = new H3(description);
        title.addClassName(CssStyles.TITLE_CARD);

        if (searchListener != null) {
            addSearchIcon(title);
        }

        setCardContent(title);
    }

    private void addSearchIcon(final H3 title) {
        final Icon icon = new Icon(VaadinIcon.SEARCH);
        icon.addClassName(CssStyles.PADDING_LEFT_10);
        icon.getElement().addEventListener("click", e -> searchListener.run());
        title.add(icon);
    }

}
