package com.flowingcode.fixture.view.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;

public class AccessUI {

    private AccessUI() {
    }

    public static void on(final Component component, final Command action) {
    	UI ui = component.getUI().orElse(null);
    	if (ui!=null && ui.getSession()!=null) {
    		ui.access(action);
    	}
    }
}
