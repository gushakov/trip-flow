package com.github.tripflow.infrastructure.config;

import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import com.github.tripflow.core.usecase.browse.BrowseActiveTripsInputPort;
import com.github.tripflow.core.usecase.browse.BrowseActiveTripsUseCase;
import com.github.tripflow.core.usecase.flight.BookFlightInputPort;
import com.github.tripflow.core.usecase.flight.BookFlightUseCase;
import com.github.tripflow.core.usecase.home.WelcomeInputPort;
import com.github.tripflow.core.usecase.home.WelcomeUseCase;
import com.github.tripflow.infrastructure.adapter.web.flight.BookFlightPresenter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for all use case beans. Each use case is a prototype scoped
 * bean which wires presenters and output ports (operations) needed to perform
 * the logic of the use case.
 */
@Configuration
public class UseCaseConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WelcomeInputPort welcomeUseCase(WelcomePresenterOutputPort presenter,
                                           SecurityOperationsOutputPort securityOps,
                                           WorkflowOperationsOutputPort workflowOps,
                                           DbPersistenceOperationsOutputPort dbOps) {

        return new WelcomeUseCase(presenter, securityOps, workflowOps, dbOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BrowseActiveTripsInputPort browseActiveTripsUseCase(BrowseActiveTripsPresenterOutputPort presenter, SecurityOperationsOutputPort securityOps,
                                                               TasksOperationsOutputPort taskOps){
        return new BrowseActiveTripsUseCase(presenter, securityOps, taskOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookFlightInputPort bookFlightUseCase(BookFlightPresenterOutputPort presenter, DbPersistenceOperationsOutputPort dbOps){
        return new BookFlightUseCase(presenter, dbOps);
    }

}
