package com.schilings.neiko.sample.authorization.server.event;


import com.schilings.neiko.security.oauth2.authorization.server.handler.ApplicationEventAuthenticationFailureHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultApplicationEventAuthenticationFailureHandler extends ApplicationEventAuthenticationFailureHandler {
    public DefaultApplicationEventAuthenticationFailureHandler(AuthenticationFailureHandler delegate) {
        super(delegate);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        getDelegate().onAuthenticationFailure(request, response, exception);
    }
}
