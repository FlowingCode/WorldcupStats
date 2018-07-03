package com.flowingcode.fixture.view.model;

import java.util.ArrayList;
import java.util.List;

public class GroupDto {

    private String groupName;

    private List<GroupDetailDto> details = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    public List<GroupDetailDto> getDetails() {
        return details;
    }

    public void setDetails(final List<GroupDetailDto> details) {
        this.details = details;
    }

    public GroupDto addDetail(final GroupDetailDto detail) {
        details.add(detail);
        return this;
    }

}
