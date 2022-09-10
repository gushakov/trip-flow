package com.github.tripflow.core.port.presenter.home;

import com.github.tripflow.core.port.error.ErrorHandlingPresenterOutputPort;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentWelcomeView(String userName);
}
