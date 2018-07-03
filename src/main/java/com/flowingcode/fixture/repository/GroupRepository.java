package com.flowingcode.fixture.repository;

import java.util.List;

import com.flowingcode.fixture.repository.domain.GroupWrapper.Group;

public interface GroupRepository {

    List<Group> getGroups();

}
