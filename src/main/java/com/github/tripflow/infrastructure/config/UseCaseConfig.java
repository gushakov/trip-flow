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
import com.github.tripflow.core.usecase.confirmation.ConfirmTripInputPort;
import com.github.tripflow.core.usecase.confirmation.ConfirmTripUseCase;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for all use case beans. Each use case is a prototype scoped
 * bean which wires presenters and other output ports needed to perform
 * the logic of the use case.
 */
@Configuration
public class UseCaseConfig {

    @Autowired
    private WelcomePresenterOutputPort welcomePresenter;

    @Autowired
    private BrowseActiveTripsPresenterOutputPort browseActiveTripsPresenter;

    @Autowired
    private BookFlightPresenterOutputPort bookFlightPresenter;

    @Autowired
    private ReserveHotelPresenterOutputPort reserveHotelPresenter;

    @Autowired
    private ViewSummaryPresenterOutputPort viewSummaryPresenter;

    @Autowired
    private SecurityOperationsOutputPort securityOps;

    @Autowired
    private WorkflowOperationsOutputPort workflowOps;

    @Autowired
    private DbPersistenceOperationsOutputPort dbOps;

    @Autowired
    private TasksOperationsOutputPort tasksOps;

    @Autowired
    private ConfigurationOperationsOutputPort configOps;

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WelcomeInputPort welcomeUseCase() {
        return new WelcomeUseCase(welcomePresenter, securityOps, workflowOps, dbOps, tasksOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BrowseActiveTripsInputPort browseActiveTripsUseCase() {
        return new BrowseActiveTripsUseCase(browseActiveTripsPresenter, securityOps, tasksOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookFlightInputPort bookFlightUseCase() {
        return new BookFlightUseCase(bookFlightPresenter, securityOps, tasksOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReserveHotelInputPort reserveHotelUseCase() {
        return new ReserveHotelUseCase(reserveHotelPresenter, securityOps, tasksOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ViewSummaryInputPort viewSummaryUseCase() {
        return new ViewSummaryUseCase(viewSummaryPresenter, securityOps, tasksOps, dbOps);
    }

    /*
        Note: the concrete implementation of ExternalJobOperationsOutputPort will be provided
        by the job handling adapter during the lookup of the use case from the application
        context.
     */

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CheckCreditInputPort checkCreditUseCase(ExternalJobOperationsOutputPort externalJobOps) {
        return new CheckCreditUseCase(externalJobOps, securityOps, configOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ConfirmTripInputPort confirmTripUseCase(ExternalJobOperationsOutputPort externalJobOps) {
        return new ConfirmTripUseCase(externalJobOps, dbOps);
    }

}
