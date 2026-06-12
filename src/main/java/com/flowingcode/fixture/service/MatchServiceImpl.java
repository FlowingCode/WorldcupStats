package com.flowingcode.fixture.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flowingcode.fixture.repository.domain.TeamEventType;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Game;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Team;
import com.flowingcode.fixture.repository.worldcup.StadiumCatalog;
import com.flowingcode.fixture.repository.worldcup.TeamCatalog;
import com.flowingcode.fixture.repository.worldcup.WorldCupClient;
import com.flowingcode.fixture.view.enums.MatchStatus;
import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.model.MatchResultDto;
import com.flowingcode.fixture.view.model.TeamDto;
import com.flowingcode.fixture.view.model.TeamEventDto;

@Service
public class MatchServiceImpl implements MatchService {

    private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    private static final String NULL = "null";

    private final WorldCupClient client;

    private final TeamCatalog teamCatalog;

    private final StadiumCatalog stadiumCatalog;

    private volatile List<LocalDate> matchDates;

    public MatchServiceImpl(final WorldCupClient client, final TeamCatalog teamCatalog, final StadiumCatalog stadiumCatalog) {
        this.client = client;
        this.teamCatalog = teamCatalog;
        this.stadiumCatalog = stadiumCatalog;
    }

    @Override
    @Cacheable("matches")
    public List<MatchResultDto> getMatches() {
        return client.getGames().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<MatchResultDto> getCurrentMatches() {
        return client.getGames().stream().filter(this::isInProgress).map(this::convert).collect(Collectors.toList());
    }

    protected MatchResultDto convert(final Game source) {
        final MatchResultDto target = new MatchResultDto();
        final ZonedDateTime kickoff = parseKickoff(source);
        target.setHomeTeam(teamName(source.home_team_id(), source.home_team_name_en()));
        target.setAwayTeam(teamName(source.away_team_id(), source.away_team_name_en()));
        target.setHomeTeamCode(teamCatalog.codeById(source.home_team_id()));
        target.setAwayTeamCode(teamCatalog.codeById(source.away_team_id()));
        target.setHomeTeamFlag(teamCatalog.flagById(source.home_team_id()));
        target.setAwayTeamFlag(teamCatalog.flagById(source.away_team_id()));
        target.setHomeTeamGoals(goals(source.home_score()));
        target.setAwayTeamGoals(goals(source.away_score()));
        target.setKickoff(kickoff);
        target.setStage(stage(source));
        target.setStageName(stageName(source));
        target.setGroupName(source.group());
        target.setFifaId(source.id());
        final MatchStatus status = status(source, kickoff);
        target.setStatus(status);
        if (status == MatchStatus.IN_PROGRESS) {
            target.setMinutes(source.time_elapsed());
        }
        return target;
    }

    @Override
    @Cacheable("matchDetail")
    public Optional<MatchDetailDto> getByFifaId(final String fifaId) {
        return client.getGames().stream().filter(g -> fifaId.equals(g.id())).findFirst().map(this::convertToDetail);
    }

    protected MatchDetailDto convertToDetail(final Game source) {
        final MatchDetailDto target = new MatchDetailDto();
        final ZonedDateTime kickoff = parseKickoff(source);
        target.setHomeTeamDto(createTeamDto(source.home_team_id(), source.home_team_name_en(), source.home_score()));
        target.setAwayTeamDto(createTeamDto(source.away_team_id(), source.away_team_name_en(), source.away_score()));
        target.setHomeTeamEvents(scorerEvents(source.home_scorers()));
        target.setAwayTeamEvents(scorerEvents(source.away_scorers()));
        target.setDateTime(kickoff);
        target.setId(source.id());
        target.setVenue(stadiumCatalog.venueById(source.stadium_id()));
        target.setLocation(stadiumCatalog.cityById(source.stadium_id()));
        target.setGroup(source.group());
        target.setStageName(stageName(source));
        final MatchStatus status = status(source, kickoff);
        target.setStatus(status);
        if (status == MatchStatus.IN_PROGRESS) {
            target.setMinutes(source.time_elapsed());
        }
        target.setTime(source.time_elapsed());
        return target;
    }

    protected TeamDto createTeamDto(final String teamId, final String nameEn, final String score) {
        final TeamDto target = new TeamDto();
        target.setCode(teamCatalog.codeById(teamId));
        target.setCountry(teamName(teamId, nameEn));
        target.setGoals(goals(score));
        return target;
    }

    /**
     * worldcup26.ir only exposes goal scorers (not full event feeds), and ships
     * them as a set-like string with curly braces and quoted entries, e.g.
     * {@code {"J. Quiñones 9'", "R. Jiménez 67'"}}. Strip that wrapping (braces
     * and straight/typographic quotes) so only the names — with their inline
     * minute — are shown.
     */
    private List<TeamEventDto> scorerEvents(final String scorers) {
        final List<TeamEventDto> events = new ArrayList<>();
        if (scorers == null || scorers.isBlank() || NULL.equalsIgnoreCase(scorers)) {
            return events;
        }
        final String unwrapped = scorers.replaceAll("[{}\"“”]", "");
        for (final String name : unwrapped.split(",")) {
            final String player = name.trim();
            if (player.isEmpty()) {
                continue;
            }
            final TeamEventDto event = new TeamEventDto();
            event.setTypeOfEvent(TeamEventType.GOAL);
            event.setPlayer(player);
            event.setTime("");
            event.setId(player);
            events.add(event);
        }
        return events;
    }

    @Override
    @Cacheable("matchesByCountry")
    public List<MatchResultDto> getByCountryCode(final String fifaCode) {
        final Team team = teamCatalog.byCode(fifaCode);
        if (team == null) {
            return Collections.emptyList();
        }
        final String id = team.id();
        return client.getGames().stream()
                .filter(g -> id.equals(g.home_team_id()) || id.equals(g.away_team_id()))
                .map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Cacheable("futureMatches")
    public List<MatchResultDto> getFutureMatches(final LocalDate startDate, final LocalDate endDate) {
        return client.getGames().stream().map(this::convert)
                .filter(m -> {
                    final LocalDate date = m.getKickoff().toLocalDate();
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> getMatchDates() {
        if (matchDates == null) {
            this.matchDates = Collections.unmodifiableList(client.getGames().stream()
                    .map(this::parseKickoff)
                    .map(ZonedDateTime::toLocalDate)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList()));
        }
        return matchDates;
    }

    @Override
    public List<MatchResultDto> getMatchesByDate(final LocalDate date) {
        return client.getGames().stream().map(this::convert)
                .filter(m -> m.getKickoff().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    // --- helpers -------------------------------------------------------------

    private String teamName(final String teamId, final String nameEn) {
        return nameEn != null && !nameEn.isBlank() ? nameEn : teamCatalog.nameById(teamId);
    }

    private ZonedDateTime parseKickoff(final Game source) {
        // The API's local_date is the venue's local wall-clock; attach the real
        // venue zone so the resulting instant is correct across host cities.
        final ZoneId zone = stadiumCatalog.zoneById(source.stadium_id());
        try {
            return LocalDateTime.parse(source.local_date(), LOCAL_DATE).atZone(zone);
        } catch (final RuntimeException e) {
            return ZonedDateTime.now(zone);
        }
    }

    private boolean isFinished(final Game source) {
        return "TRUE".equalsIgnoreCase(source.finished());
    }

    private boolean isInProgress(final Game source) {
        if (isFinished(source)) {
            return false;
        }
        final String elapsed = source.time_elapsed();
        return elapsed != null && !elapsed.isBlank() && !"notstarted".equalsIgnoreCase(elapsed);
    }

    private MatchStatus status(final Game source, final ZonedDateTime kickoff) {
        if (isFinished(source)) {
            return MatchStatus.COMPLETED;
        }
        if (isInProgress(source)) {
            return MatchStatus.IN_PROGRESS;
        }
        if (kickoff.toLocalDate().equals(LocalDate.now())) {
            return MatchStatus.TODAY;
        }
        return MatchStatus.FUTURE;
    }

    private String goals(final String score) {
        return score == null || NULL.equalsIgnoreCase(score) ? "0" : score;
    }

    private boolean isGroupStage(final Game source) {
        return source.type() == null || "group".equalsIgnoreCase(source.type());
    }

    private String stageName(final Game source) {
        return isGroupStage(source) ? "First stage" : prettyStage(source.type());
    }

    private String stage(final Game source) {
        return isGroupStage(source) ? "Matchday " + source.matchday() : stageName(source);
    }

    private static final Pattern ROUND_OF = Pattern.compile("(?i)round[\\s_-]*(?:of)?[\\s_-]*(\\d+)");

    /**
     * Maps the API's {@code type} value to a human-readable stage name. It is
     * data-driven: known knockout stages get canonical labels, and any other
     * value (including ones not yet seen, since the API currently only returns
     * "group") is humanized rather than hardcoded — so it renders sensibly the
     * moment knockout fixtures appear, with no code change required.
     */
    private String prettyStage(final String type) {
        final String trimmed = type.trim();
        final Matcher round = ROUND_OF.matcher(trimmed);
        if (round.matches()) {
            return "Round of " + round.group(1);
        }
        switch (trimmed.toLowerCase().replaceAll("[\\s_-]", "")) {
            case "quarter":
            case "quarterfinal":
            case "quarterfinals":
                return "Quarter-finals";
            case "semi":
            case "semifinal":
            case "semifinals":
                return "Semi-finals";
            case "third":
            case "thirdplace":
                return "Third place";
            case "final":
                return "Final";
            default:
                return humanize(trimmed);
        }
    }

    /** Turns an arbitrary token ("roundOf16", "quarter_final", "play-off") into "Title Case Words". */
    private String humanize(final String value) {
        final String spaced = value
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Za-z])(\\d)", "$1 $2")
                .replaceAll("[_-]+", " ")
                .trim();
        final StringBuilder builder = new StringBuilder();
        for (final String word : spaced.split("\\s+")) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
        }
        return builder.length() == 0 ? value : builder.toString();
    }

}
