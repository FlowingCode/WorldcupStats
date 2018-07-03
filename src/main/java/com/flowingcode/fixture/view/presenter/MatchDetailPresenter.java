package com.flowingcode.fixture.view.presenter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.flowingcode.fixture.infra.HasLogger;
import com.flowingcode.fixture.service.MatchService;
import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.screen.MatchDetailScreen;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class MatchDetailPresenter implements HasLogger {

    @Autowired
    private MatchService matchService;

    private MatchDetailScreen view;

    public void setView(final MatchDetailScreen view) {
        this.view = view;
    }

    public void loadResults(final String fifaId) {
        final Optional<MatchDetailDto> match = matchService.getByFifaId(fifaId);
        if (match.isPresent()) {
            view.init(match.get());
        } else {
            logger().warn("Match not found for fifaId: " + fifaId);
            view.goHome();
        }
    }

}
