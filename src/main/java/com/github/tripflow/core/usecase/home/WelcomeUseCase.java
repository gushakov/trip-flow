package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    private final WelcomePresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;

    @Override
    public void welcomeUser() {

        String userName = securityOps.loggedInUserName();

        presenter.presentWelcomeView(userName);

    }
}
