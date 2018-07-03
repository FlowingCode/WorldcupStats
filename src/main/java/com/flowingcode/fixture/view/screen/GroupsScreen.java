package com.flowingcode.fixture.view.screen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.fixture.view.component.GroupView;
import com.flowingcode.fixture.view.model.GroupDto;
import com.flowingcode.fixture.view.presenter.GroupsPresenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "groups", layout = MainLayout.class)
public class GroupsScreen extends VerticalLayout {

    private final VerticalLayout groupsContainer = new VerticalLayout();

    private final GroupsPresenter presenter;

    @Autowired
    public GroupsScreen(final GroupsPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);

        groupsContainer.setPadding(false);
        groupsContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
 
        this.add(groupsContainer);
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        presenter.loadGroups();
    }

    public void setGroups(final List<GroupDto> groups) {
        for (final GroupDto group : groups) {
            final GroupView groupView = new GroupView();
            groupView.init(group);
            groupsContainer.add(groupView);
        }
    }

}
