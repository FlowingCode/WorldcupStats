package com.flowingcode.fixture.view.model;

/**
 * Model that represents a team
 * 
 * @author mlopez
 *
 */
public class TeamDto {

	private String country;
	private String code;
	private String goals;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGoals() {
		return goals;
	}
	public void setGoals(String goals) {
		this.goals = goals;
	}
	
}
