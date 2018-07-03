package com.flowingcode.fixture.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flowingcode.fixture.repository.MatchesRepository;
import com.flowingcode.fixture.repository.TeamRepository;
import com.flowingcode.fixture.repository.domain.Match;
import com.flowingcode.fixture.repository.domain.Match.Team;
import com.flowingcode.fixture.repository.domain.Match.Team_event;
import com.flowingcode.fixture.repository.domain.Match.Team_statistics;
import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.model.TeamDto;
import com.flowingcode.fixture.view.model.TeamEventDto;
import com.flowingcode.fixture.view.model.TeamStatisticsDto;

@Service
public class MatchServiceImpl implements MatchService {

    private static final String FUTURE = "future";

    private static final String COMPLETED = "completed";

    private static final String IN_PROGRESS = "in progress";

    private static final String FULL_TIME = "full-time";

    private static final Predicate<Match> IS_IN_PROGRESS = match -> IN_PROGRESS.equals(match.status) && !FULL_TIME.equals(match.time);

    private static final Predicate<Match> IS_COMPLETED = match -> COMPLETED.equals(match.status) || FULL_TIME.equals(match.time);

    private static final Predicate<Match> IS_FUTURE = match -> FUTURE.equals(match.status);

    @Autowired
    private MatchesRepository matchesRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Map<String, String> countryGroupMap;

    private volatile List<LocalDate> matchDates;

    @Override
    @Cacheable("matches")
    public List<MatchResultDto> getMatches() {
        return matchesRepository.getMatches().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<MatchResultDto> getCurrentMatches() {
        return matchesRepository.getCurrentMatches().stream().map(this::convert).collect(Collectors.toList());
    }

    protected MatchResultDto convert(final Match source) {
        final MatchResultDto target = new MatchResultDto();
        target.setAwayTeam(source.away_team.country);
        target.setAwayTeamFlag(FlagUtils.getFlagForFifaCode(source.away_team.code));
        target.setAwayTeamGoals(String.valueOf(source.away_team.goals));
        target.setAwayTeamCode(source.away_team.code);
        target.setHomeTeam(source.home_team.country);
        target.setHomeTeamFlag(FlagUtils.getFlagForFifaCode(source.home_team.code));
        target.setHomeTeamGoals(String.valueOf(source.home_team.goals));
        target.setHomeTeamCode(source.home_team.code);
        target.setKickoff(source.datetime);
        target.setStage(source.location);
        target.setFifaId(source.fifa_id);
        target.setGroupName(getGroup(source.away_team.code));
        target.setStageName(source.stage_name);
        if (IS_IN_PROGRESS.test(source)) {
            target.setStatus(MatchStatus.IN_PROGRESS);
            target.setMinutes(source.time);
        }
        if (IS_COMPLETED.test(source)) {
            target.setStatus(MatchStatus.COMPLETED);
        }
        if (IS_FUTURE.test(source)) {
            target.setStatus(MatchStatus.FUTURE);
        }
        return target;
    }

    @Override
    @Cacheable("matchDetail")
    public Optional<MatchDetailDto> getByFifaId(final String fifaId) {
        return matchesRepository.getByFifaID(fifaId).map(this::convertToDetail);
    }

    protected MatchDetailDto convertToDetail(final Match source) {
        final MatchDetailDto target = new MatchDetailDto();
        target.setAwayTeamDto(createTeamDto(source.away_team));
        target.setAwayTeamEvents(source.away_team_events.stream().map(this::createTeamEventDto).collect(Collectors.toList()));
        target.setAwayTeamStatistics(createTeamsStatisticsDto(source.away_team_statistics));
        target.setHomeTeamDto(createTeamDto(source.home_team));
        target.setHomeTeamEvents(source.home_team_events.stream().map(this::createTeamEventDto).collect(Collectors.toList()));
        target.setHomeTeamStatistics(createTeamsStatisticsDto(source.home_team_statistics));
        target.setDateTime(source.datetime);
        target.setId(source.fifa_id);
        target.setLocation(source.location);
        target.setVenue(source.venue);
        target.setGroup(getGroup(source.away_team.code));
        target.setStageName(source.stage_name);
        if (IS_IN_PROGRESS.test(source)) {
            target.setStatus(MatchStatus.IN_PROGRESS);
            target.setMinutes(source.time);
        }
        if (IS_COMPLETED.test(source)) {
            target.setStatus(MatchStatus.COMPLETED);
        }
        if (IS_FUTURE.test(source)) {
            target.setStatus(MatchStatus.FUTURE);
        }
        target.setTime(source.time);
        target.setWinner(source.winner);
        target.setWinnerCode(source.winner_code);
        return target;
    }

    protected TeamDto createTeamDto(final Team source) {
        final TeamDto target = new TeamDto();
        target.setCode(source.code);
        target.setCountry(source.country);
        target.setGoals(String.valueOf(source.goals));
        return target;
    }

    protected TeamEventDto createTeamEventDto(final Team_event source) {
        final TeamEventDto target = new TeamEventDto();
        target.setId(String.valueOf(source.id));
        target.setPlayer(source.player);
        target.setTime(source.time);
        target.setTypeOfEvent(source.type_of_event);
        return target;
    }

    protected TeamStatisticsDto createTeamsStatisticsDto(final Team_statistics source) {
        final TeamStatisticsDto target = new TeamStatisticsDto();
        target.setAttemptsOnGoal(source.attempts_on_goal);
        target.setBallPossession(source.ball_possession);
        target.setBallsRecovered(source.balls_recovered);
        target.setBlocked(source.blocked);
        target.setClearances(source.clearances);
        target.setCorners(source.corners);
        target.setCountry(source.country);
        target.setDistanceCovered(source.distance_covered);
        target.setFoulsCommitted(source.fouls_committed);
        target.setNumPasses(source.num_passes);
        target.setOffsides(source.offsides);
        target.setOffTarget(source.off_target);
        target.setOnTarget(source.on_target);
        target.setPassAccuracy(source.pass_accuracy);
        target.setPassesCompleted(source.passes_completed);
        target.setRedCards(source.red_cards);
        target.setTackles(source.tackles);
        target.setWoodwork(source.woodwork);
        target.setYellowCards(source.yellow_cards);
        return target;
    }

    @Override
    @Cacheable("matchesByCountry")
    public List<MatchResultDto> getByCountryCode(final String fifaCode) {
        return matchesRepository.getByCountryCode(fifaCode).stream().map(this::convert).collect(Collectors.toList());
    }

    protected String getGroup(final String countryCode) {
        if (countryGroupMap == null) {
            countryGroupMap = teamRepository.getTeams().stream().collect(Collectors.toMap(t -> t.fifa_code, t -> t.group_letter));
        }
        return countryGroupMap.get(countryCode);
    }

    @Override
    @Cacheable("futureMatches")
    public List<MatchResultDto> getFutureMatches(final LocalDate startDate, final LocalDate endDate) {
        return matchesRepository.getMatches(startDate, endDate).stream().filter(m -> !m.home_team.code.equals("TBD") || !m.away_team.code.equals("TBD"))
                .map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> getMatchDates() {
        if (matchDates == null) {
            final List<LocalDate> matchDates = matchesRepository.getMatches().stream()
                    .map(m -> m.datetime)
                    .map(ZonedDateTime::toLocalDate)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());
            this.matchDates = Collections.unmodifiableList(matchDates);
        }
        return matchDates;
    }

    @Override
    public List<MatchResultDto> getMatchesByDate(final LocalDate date) {
        return matchesRepository.getMatches(date, date).stream().map(this::convert).collect(Collectors.toList());
    }

}
