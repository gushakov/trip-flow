package com.github.tripflow.infrastructure.config;

import com.github.tripflow.core.port.operation.config.ConfigurationOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import com.github.tripflow.core.port.presenter.summary.ViewSummaryPresenterOutputPort;
import com.github.tripflow.core.usecase.browse.BrowseActiveTripsInputPort;
import com.github.tripflow.core.usecase.browse.BrowseActiveTripsUseCase;
import com.github.tripflow.core.usecase.creditcheck.CheckCreditInputPort;
import com.github.tripflow.core.usecase.creditcheck.CheckCreditUseCase;
import com.github.tripflow.core.usecase.flight.BookFlightInputPort;
import com.github.tripflow.core.usecase.flight.BookFlightUseCase;
import com.github.tripflow.core.usecase.home.WelcomeInputPort;
import com.github.tripflow.core.usecase.home.WelcomeUseCase;
import com.github.tripflow.core.usecase.hotel.ReserveHotelInputPort;
import com.github.tripflow.core.usecase.hotel.ReserveHotelUseCase;
import com.github.tripflow.core.usecase.summary.ViewSummaryInputPort;
import com.github.tripflow.core.usecase.summary.ViewSummaryUseCase;
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
                                           DbPersistenceOperationsOutputPort dbOps,
                                           TasksOperationsOutputPort tasksOps) {

        return new WelcomeUseCase(presenter, securityOps, workflowOps, dbOps, tasksOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BrowseActiveTripsInputPort browseActiveTripsUseCase(BrowseActiveTripsPresenterOutputPort presenter,
                                                               SecurityOperationsOutputPort securityOps,
                                                               TasksOperationsOutputPort tasksOps,
                                                               DbPersistenceOperationsOutputPort dbOps) {
        return new BrowseActiveTripsUseCase(presenter, securityOps, tasksOps, dbOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookFlightInputPort bookFlightUseCase(BookFlightPresenterOutputPort presenter,
                                                 SecurityOperationsOutputPort securityOps,
                                                 TasksOperationsOutputPort tasksOps,
                                                 DbPersistenceOperationsOutputPort dbOps) {
        return new BookFlightUseCase(presenter, securityOps, tasksOps, dbOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReserveHotelInputPort reserveHotelUseCase(ReserveHotelPresenterOutputPort presenter,
                                                     SecurityOperationsOutputPort securityOps,
                                                     TasksOperationsOutputPort tasksOps,
                                                     DbPersistenceOperationsOutputPort dbOps) {
        return new ReserveHotelUseCase(presenter, securityOps, tasksOps, dbOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ViewSummaryInputPort viewSummaryUseCase(ViewSummaryPresenterOutputPort presenter,
                                                   SecurityOperationsOutputPort securityOps,
                                                   TasksOperationsOutputPort tasksOps,
                                                   DbPersistenceOperationsOutputPort dbOps) {
        return new ViewSummaryUseCase(presenter, securityOps, tasksOps, dbOps);
    }

    // the concrete implementation of ExternalJobOperationsOutputPort will be provided
    // by the job handling adapter during the lookup of the use case from the application
    // context

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CheckCreditInputPort checkCreditUseCase(ExternalJobOperationsOutputPort externalJobOps,
                                                   SecurityOperationsOutputPort securityOps,
                                                   ConfigurationOperationsOutputPort configOps,
                                                   DbPersistenceOperationsOutputPort dbOps){
        return new CheckCreditUseCase(externalJobOps, securityOps, configOps, dbOps);
    }

}
