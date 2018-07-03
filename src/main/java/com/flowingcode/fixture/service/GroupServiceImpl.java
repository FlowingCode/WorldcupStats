package com.flowingcode.fixture.service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flowingcode.fixture.repository.GroupRepository;
import com.flowingcode.fixture.repository.domain.GroupWrapper.Group;
import com.flowingcode.fixture.repository.domain.GroupWrapper.Group.TeamWrapper;
import com.flowingcode.fixture.repository.domain.GroupWrapper.Group.TeamWrapper.Team;
import com.flowingcode.fixture.view.model.GroupDetailDto;
import com.flowingcode.fixture.view.model.GroupDto;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    @Cacheable("groups")
    public List<GroupDto> getGroups() {
        return groupRepository.getGroups().stream().map(this::convertGroup).sorted(Comparator.comparing(GroupDto::getGroupName)).collect(Collectors.toList());
    }

    protected GroupDto convertGroup(final Group source) {
        final GroupDto target = new GroupDto();
        target.setGroupName(source.letter);
        final AtomicInteger position = new AtomicInteger(1);
        target.setDetails(
                source.ordered_teams.stream().map(e -> convert(e, position)).sorted(Comparator.comparingInt(GroupDetailDto::getPosition)).collect(Collectors.toList()));
        return target;
    }

    protected GroupDetailDto convert(final TeamWrapper.Team source, final AtomicInteger position) {
        final GroupDetailDto target = new GroupDetailDto();
        target.setTeamName(source.country);
        target.setFifaCode(source.fifa_code);
        target.setGoalDifference(source.goal_differential);
        target.setGoalsAgainst(source.goals_against);
        target.setGoalsFor(source.goals_for);
        target.setMatchesPlayed(source.games_played);
        target.setMatchesDrawn(source.draws);
        target.setMatchesLost(source.losses);
        target.setMatchesWon(source.wins);
        target.setPoints(source.points);
        target.setPosition(position.getAndIncrement());
        target.setTeamLogo(FlagUtils.getFlagForFifaCode(source.fifa_code));
        return target;
    }

}
