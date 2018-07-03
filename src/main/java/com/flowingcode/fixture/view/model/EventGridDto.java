package com.flowingcode.fixture.view.model;

/**
 * Model for events grid
 * 
 * @author mlopez
 *
 */
public class EventGridDto {
	
	private TeamEventDto homeTeamEvent;
	private TeamEventDto awayTeamEvent;
	
	public EventGridDto(TeamEventDto homeTeamEvent, TeamEventDto awayTeamEvent) {
		this.homeTeamEvent = homeTeamEvent;
		this.awayTeamEvent = awayTeamEvent;
	}
	public TeamEventDto getHomeTeamEvent() {
		return homeTeamEvent;
	}
	public void setHomeTeamEvent(TeamEventDto homeTeamEvent) {
		this.homeTeamEvent = homeTeamEvent;
	}
	public TeamEventDto getAwayTeamEvent() {
		return awayTeamEvent;
	}
	public void setAwayTeamEvent(TeamEventDto awayTeamEvent) {
		this.awayTeamEvent = awayTeamEvent;
	}

}
