package com.schilings.neiko.security.oauth2.authorization.server.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NullEventAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {

	public NullEventAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		this.getDelegate().onAuthenticationSuccess(request, response, authentication);
	}

}
