package com.flowingcode.fixture.view.screen;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.presenter.MatchesPresenter;
import com.flowingcode.fixture.view.util.DateTimeUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
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
		layout.add(new Label("Filter by date"));
		combobox = new ComboBox<>();
		combobox.setItems(matchService.getMatchDates());
		combobox.setItemLabelGenerator(DateTimeUtil::styleDate);
		combobox.setWidth("176px");
		
		layout.add(combobox);
		Button acceptButton = new Button("Accept", ev-> accept());
		Button cancelButton = new Button("Cancel", ev-> cancel());
		acceptButton.setWidth("80px");
		cancelButton.setWidth("80px");
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
		dialog.open();
	}
	
}
