package com.flowingcode.fixture.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

import com.flowingcode.fixture.repository.domain.GroupWrapper;
import com.flowingcode.fixture.repository.domain.GroupWrapper.Group;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    @Override
    public List<Group> getGroups() {
        final RestOperations template = new SFGRestTemplate();
        final ResponseEntity<Group[]> responseEntity = template.getForEntity("http://worldcup.sfg.io/teams/group_results", Group[].class);
        return Arrays.asList(responseEntity.getBody());
    }

}
