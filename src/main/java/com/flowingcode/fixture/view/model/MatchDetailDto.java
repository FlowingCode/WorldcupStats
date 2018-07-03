package com.flowingcode.fixture.view.model;

import java.time.ZonedDateTime;
import java.util.List;

import com.flowingcode.fixture.service.FlagUtils;
import com.flowingcode.fixture.view.enums.MatchStatus;

/**
 * Model that represents the details of a specific football match
 *
 * @author mlopez
 *
 */
public class MatchDetailDto implements MatchResume {

	private String id;
	private String venue;
	private String location;
    private ZonedDateTime dateTime;
    private MatchStatus status;
	private String time;
	private TeamDto homeTeam;
	private TeamDto awayTeam;
	private String winner;
	private String winnerCode;
	private List<TeamEventDto> homeTeamEvents;
	private List<TeamEventDto> awayTeamEvents;
    private TeamStatisticsDto homeTeamStatistics;
    private TeamStatisticsDto awayTeamStatistics;
    private String group;
    private String minutes;
    private String stageName;

	public String getId() {
		return id;
	}
	public void setId(final String id) {
		this.id = id;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(final String venue) {
		this.venue = venue;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(final String location) {
		this.location = location;
	}

    public ZonedDateTime getDateTime() {
		return dateTime;
	}

    public void setDateTime(final ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}

    @Override
    public MatchStatus getStatus() {
		return status;
	}

    public void setStatus(final MatchStatus status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(final String time) {
		this.time = time;
	}

    public TeamDto getHomeTeamDto() {
		return homeTeam;
	}

    public void setHomeTeamDto(final TeamDto homeTeam) {
		this.homeTeam = homeTeam;
	}

    public TeamDto getAwayTeamDto() {
		return awayTeam;
	}

    public void setAwayTeamDto(final TeamDto awayTeam) {
		this.awayTeam = awayTeam;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(final String winner) {
		this.winner = winner;
	}
	public String getWinnerCode() {
		return winnerCode;
	}
	public void setWinnerCode(final String winnerCode) {
		this.winnerCode = winnerCode;
	}
	public List<TeamEventDto> getHomeTeamEvents() {
		return homeTeamEvents;
	}
	public void setHomeTeamEvents(final List<TeamEventDto> homeTeamEvents) {
		this.homeTeamEvents = homeTeamEvents;
	}
	public List<TeamEventDto> getAwayTeamEvents() {
		return awayTeamEvents;
	}
	public void setAwayTeamEvents(final List<TeamEventDto> awayTeamEvents) {
		this.awayTeamEvents = awayTeamEvents;
	}

    public TeamStatisticsDto getHomeTeamStatistics() {
        return homeTeamStatistics;
    }

    public void setHomeTeamStatistics(final TeamStatisticsDto homeTeamStatistics) {
        this.homeTeamStatistics = homeTeamStatistics;
    }

    public TeamStatisticsDto getAwayTeamStatistics() {
        return awayTeamStatistics;
    }

    public void setAwayTeamStatistics(final TeamStatisticsDto awayTeamStatistics) {
        this.awayTeamStatistics = awayTeamStatistics;
    }

    @Override
    public String getStage() {
        return location;
    }

    @Override
    public String getGroupName() {
        return group;
    }

    @Override
    public String getHomeTeam() {
        return homeTeam.getCountry();
    }

    @Override
    public String getAwayTeam() {
        return awayTeam.getCountry();
    }

    @Override
    public String getHomeTeamGoals() {
        return homeTeam.getGoals();
    }

    @Override
    public String getAwayTeamGoals() {
        return awayTeam.getGoals();
    }

    @Override
    public ZonedDateTime getKickoff() {
        return dateTime;
    }

    @Override
    public String getMinutes() {
        return minutes;
    }

    @Override
    public String getHomeTeamFlag() {
        return FlagUtils.getFlagForFifaCode(homeTeam.getCode());
    }

    @Override
    public String getAwayTeamFlag() {
        return FlagUtils.getFlagForFifaCode(awayTeam.getCode());
    }

    @Override
    public String getFifaId() {
        return id;
    }

    @Override
    public String getHomeTeamCode() {
        return homeTeam.getCode();
    }

    @Override
    public String getAwayTeamCode() {
        return awayTeam.getCode();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public void setMinutes(final String minutes) {
        this.minutes = minutes;
    }
    
	@Override
	public String getStageName() {
		return stageName;
	}
	
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
}
