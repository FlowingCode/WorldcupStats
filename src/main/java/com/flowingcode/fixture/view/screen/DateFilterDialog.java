package com.flowingcode.fixture.view.screen;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class DateFilterDialog  {
	
	private final ComboBox<LocalDate> combobox;
	
	private final Dialog dialog;
	
	private SerializableConsumer<LocalDate> callback;
	
	public DateFilterDialog(@Autowired MatchService matchService) {
		dialog = new Dialog();
		VerticalLayout layout = new VerticalLayout();
		layout.add(new Span("Filter by date"));
		combobox = new ComboBox<>();
		combobox.setItems(matchService.getMatchDates());
		combobox.setItemLabelGenerator(DateTimeUtil::styleDate);
		combobox.setWidth("176px");
		
		layout.add(combobox);
		Button acceptButton = new Button("Accept", ev-> accept());
		Button cancelButton = new Button("Cancel", ev-> cancel());
		HorizontalLayout buttons = new HorizontalLayout(acceptButton, cancelButton);
		buttons.setSpacing(true);
		
		layout.add(buttons);
		dialog.add(layout);
	}

	private void cancel() {
		dialog.close();
	}

	private void accept() {
		callback.accept(combobox.getValue());
		dialog.close();
	}
	
	public void open(SerializableConsumer<LocalDate> callback) {
		this.callback = Objects.requireNonNull(callback);
		// Defensive: make sure the dialog is attached to the current UI before
		// opening, in case open() does not auto-attach it.
		if (!dialog.isAttached()) {
			UI.getCurrent().add(dialog);
		}
		dialog.open();
	}
	
}
