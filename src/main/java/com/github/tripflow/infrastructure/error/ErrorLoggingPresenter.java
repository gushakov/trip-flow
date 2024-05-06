package com.github.tripflow.infrastructure.error;

import com.github.tripflow.core.port.ErrorHandlingPresenterOutputPort;
import org.springframework.stereotype.Component;

@Component
public class ErrorLoggingPresenter extends AbstractErrorHandler implements ErrorHandlingPresenterOutputPort {
    @Override
    public void presentError(Exception e) {
        logError(e);
    }
}
