package com.flowingcode.fixture.view.model;

import com.flowingcode.fixture.repository.domain.TeamEventType;

/**
 * Model that represents a single event during a match from a specific Team
 *
 * @author mlopez
 *
 */
public class TeamEventDto {

	private String id;
    private TeamEventType typeOfEvent;
	private String player;
	private String time;

	public String getId() {
		return id;
	}
	public void setId(final String id) {
		this.id = id;
	}

    public TeamEventType getTypeOfEvent() {
		return typeOfEvent;
	}

    public void setTypeOfEvent(final TeamEventType typeOfEvent) {
		this.typeOfEvent = typeOfEvent;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(final String player) {
		this.player = player;
	}
	public String getTime() {
		return time;
	}
	public void setTime(final String time) {
		this.time = time;
	}

}
