package com.flowingcode.fixture.view.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.flowingcode.fixture.service.GroupService;
import com.flowingcode.fixture.view.screen.GroupsScreen;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class GroupsPresenter {

    private final GroupService groupService;

    private GroupsScreen view;

    @Autowired
    public GroupsPresenter(final GroupService groupService) {
        this.groupService = groupService;
    }

    public void setView(final GroupsScreen view) {
        this.view = view;
    }

    public void loadGroups() {
        view.setGroups(groupService.getGroups());
    }

}
