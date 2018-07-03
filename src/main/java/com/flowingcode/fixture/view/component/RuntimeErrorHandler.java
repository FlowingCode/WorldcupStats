package com.flowingcode.fixture.view.component;

import javax.servlet.http.HttpServletResponse;

import com.flowingcode.fixture.infra.HasLogger;
import com.flowingcode.fixture.view.screen.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

@ParentLayout(MainLayout.class)
@Tag(Tag.DIV)
public class RuntimeErrorHandler extends Component implements HasErrorParameter<RuntimeException>, HasLogger {

    @Override
    public int setErrorParameter(final BeforeEnterEvent event,
            final ErrorParameter<RuntimeException> parameter) {
        logger().error("Unexpected error", parameter.getException());
        getElement().setText("Page not found");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}