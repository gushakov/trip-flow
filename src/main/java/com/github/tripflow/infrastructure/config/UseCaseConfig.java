package com.github.tripflow.infrastructure.config;

import com.github.tripflow.core.port.operation.config.ConfigurationOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import com.github.tripflow.core.port.presenter.outcome.ViewOutcomePresenterOutputPort;
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
import com.github.tripflow.core.usecase.outcome.ViewOutcomeInputPort;
import com.github.tripflow.core.usecase.outcome.ViewOutcomeUseCase;
import com.github.tripflow.core.usecase.summary.ViewSummaryInputPort;
import com.github.tripflow.core.usecase.summary.ViewSummaryUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for all use case beans. Each use case is a prototype scoped
 * bean which wires presenters and/or other output ports needed to perform
 * the logic of the use case.
 */
@Configuration
public class UseCaseConfig {

    @Autowired
    private SecurityOperationsOutputPort securityOps;

    @Autowired
    private WorkflowOperationsOutputPort workflowOps;

    @Autowired
    private DbPersistenceOperationsOutputPort dbOps;

    @Autowired
    private ConfigurationOperationsOutputPort configOps;

    // use cases for user tasks (run with user participation)

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WelcomeInputPort welcomeUseCase(WelcomePresenterOutputPort welcomePresenter) {
        return new WelcomeUseCase(welcomePresenter, securityOps, workflowOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BrowseActiveTripsInputPort browseActiveTripsUseCase(BrowseActiveTripsPresenterOutputPort browseActiveTripsPresenter) {
        return new BrowseActiveTripsUseCase(browseActiveTripsPresenter, securityOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookFlightInputPort bookFlightUseCase(BookFlightPresenterOutputPort bookFlightPresenter) {
        return new BookFlightUseCase(bookFlightPresenter, securityOps, dbOps, workflowOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReserveHotelInputPort reserveHotelUseCase(ReserveHotelPresenterOutputPort reserveHotelPresenter) {
        return new ReserveHotelUseCase(reserveHotelPresenter, securityOps, dbOps, workflowOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ViewSummaryInputPort viewSummaryUseCase(ViewSummaryPresenterOutputPort viewSummaryPresenter) {
        return new ViewSummaryUseCase(viewSummaryPresenter, securityOps, dbOps, workflowOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ViewOutcomeInputPort viewOutcomeUseCase(ViewOutcomePresenterOutputPort viewOutcomePresenter) {
        return new ViewOutcomeUseCase(viewOutcomePresenter, dbOps, workflowOps);
    }


    // use cases for service tasks (run unattended)

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CheckCreditInputPort checkCreditUseCase() {
        return new CheckCreditUseCase(workflowOps, securityOps, configOps, dbOps);
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ConfirmTripInputPort confirmTripUseCase() {
        return new ConfirmTripUseCase(workflowOps, dbOps);
    }

}
