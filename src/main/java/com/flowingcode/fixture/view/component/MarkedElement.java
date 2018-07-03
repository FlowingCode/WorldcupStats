package com.flowingcode.fixture.view.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;

@SuppressWarnings("serial")
@HtmlImport("bower_components/marked-element/marked-element.html")
@Tag("marked-element")
public class MarkedElement extends Component implements HasSize {
	
	public MarkedElement(String markdown) {
		this.setMarkdown(markdown);
		setSizeFull();
	}
	
	public void setMarkdown(String markdown) {
		this.getElement().setAttribute("markdown", markdown);
	}

}
