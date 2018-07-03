package com.flowingcode.fixture.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

import com.flowingcode.fixture.repository.domain.Team;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    @Override
    public List<Team> getTeams() {
        final RestOperations template = new SFGRestTemplate();
        final ResponseEntity<Team[]> responseEntity = template.getForEntity("http://worldcup.sfg.io/teams", Team[].class);
        return Arrays.asList(responseEntity.getBody());
    }

}
