package com.github.tripflow.infrastructure.error;

import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;
import org.springframework.stereotype.Component;

@Component
public class GenericPresenter extends AbstractErrorHandler implements ErrorHandlingPresenterOutputPort {
    @Override
    public void presentError(Exception e) {
        logErrorAndRollback(e);
    }
}
