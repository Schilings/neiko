package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NullEventAuthenticationFailureHandler extends ApplicationEventAuthenticationFailureHandler {

	public NullEventAuthenticationFailureHandler(AuthenticationFailureHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		this.getDelegate().onAuthenticationFailure(request, response, exception);
	}

}
