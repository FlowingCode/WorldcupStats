package com.flowingcode.fixture.view.component;

import javax.servlet.http.HttpServletResponse;

import com.flowingcode.fixture.view.screen.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteNotFoundError;

@ParentLayout(MainLayout.class)
@Tag(Tag.DIV)
public class RouteNotFoundErrorHandler extends RouteNotFoundError {

    @Override
    public int setErrorParameter(final BeforeEnterEvent event,
            final ErrorParameter<NotFoundException> parameter) {
        getElement().setText("Page not found");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}