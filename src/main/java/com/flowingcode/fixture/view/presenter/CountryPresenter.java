package com.flowingcode.fixture.view.presenter;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.screen.CountryScreen;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CountryPresenter {

	@Autowired	
	private MatchService matchService;
    
	private CountryScreen view;

    public void setView(final CountryScreen view) {
        this.view = view;
    }

    public void loadResults(String country) {
    	final List<MatchResultDto> resultsList = matchService.getByCountryCode(country);        
        view.init(resultsList);
    }

}
