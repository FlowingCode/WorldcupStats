package com.flowingcode.fixture.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.Group;
import com.flowingcode.fixture.repository.worldcup.Wc26Dtos.GroupTeam;
import com.flowingcode.fixture.repository.worldcup.TeamCatalog;
import com.flowingcode.fixture.repository.worldcup.WorldCupClient;
import com.flowingcode.fixture.view.model.GroupDetailDto;
import com.flowingcode.fixture.view.model.GroupDto;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Comparator<GroupDetailDto> STANDINGS_ORDER =
            Comparator.comparingInt(GroupDetailDto::getPoints).reversed()
                    .thenComparing(Comparator.comparingInt(GroupDetailDto::getGoalDifference).reversed())
                    .thenComparing(Comparator.comparingInt(GroupDetailDto::getGoalsFor).reversed());

    private final WorldCupClient client;

    private final TeamCatalog teamCatalog;

    public GroupServiceImpl(final WorldCupClient client, final TeamCatalog teamCatalog) {
        this.client = client;
        this.teamCatalog = teamCatalog;
    }

    @Override
    @Cacheable("groups")
    public List<GroupDto> getGroups() {
        return client.getGroups().stream().map(this::convertGroup)
                .sorted(Comparator.comparing(GroupDto::getGroupName)).collect(Collectors.toList());
    }

    protected GroupDto convertGroup(final Group source) {
        final GroupDto target = new GroupDto();
        target.setGroupName(source.name());
        final List<GroupDetailDto> details = source.teams().stream().map(this::convert)
                .sorted(STANDINGS_ORDER).collect(Collectors.toList());
        int position = 1;
        for (final GroupDetailDto detail : details) {
            detail.setPosition(position++);
        }
        target.setDetails(details);
        return target;
    }

    protected GroupDetailDto convert(final GroupTeam source) {
        final GroupDetailDto target = new GroupDetailDto();
        target.setTeamName(teamCatalog.nameById(source.team_id()));
        target.setFifaCode(teamCatalog.codeById(source.team_id()));
        target.setTeamLogo(teamCatalog.flagById(source.team_id()));
        target.setMatchesPlayed(parseInt(source.mp()));
        target.setMatchesWon(parseInt(source.w()));
        target.setMatchesDrawn(parseInt(source.d()));
        target.setMatchesLost(parseInt(source.l()));
        target.setGoalsFor(parseInt(source.gf()));
        target.setGoalsAgainst(parseInt(source.ga()));
        target.setGoalDifference(parseInt(source.gd()));
        target.setPoints(parseInt(source.pts()));
        return target;
    }

    private static int parseInt(final String value) {
        try {
            return value == null || value.isBlank() ? 0 : Integer.parseInt(value.trim());
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

}
