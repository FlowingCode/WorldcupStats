package com.flowingcode.fixture.view.component;

import com.flowingcode.addons.applayout.PaperCard;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.SerializableRunnable;

@SuppressWarnings("serial")
public class TitleComponent extends PaperCard {
	
	private final SerializableRunnable searchListener;
	
	public TitleComponent(String description) {
		this(description,null);
	}
	
	public TitleComponent(String description, SerializableRunnable searchListener) {
		this.searchListener = searchListener;
		
		addClassName("common-card");
		H3 title = new H3(description);
		title.addClassName("title-card");
		
		if(searchListener!=null) {
			addSearchIcon(title);
		}
		
		setCardContent(title);
	}
	
	private void addSearchIcon(H3 title) {
		Icon icon = new Icon(VaadinIcon.SEARCH);
		icon.addClassName("paading-lef-10px");
		icon.getElement().addEventListener("click", e -> searchListener.run());
		title.add(icon);
	}
	
	
}
