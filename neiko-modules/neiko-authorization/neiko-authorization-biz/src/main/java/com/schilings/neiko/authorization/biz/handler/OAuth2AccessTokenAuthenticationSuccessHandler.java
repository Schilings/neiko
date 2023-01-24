package com.schilings.neiko.authorization.biz.handler;

import com.schilings.neiko.authorization.common.event.OAuth2AccessTokenAuthenticationSuccessEvent;
import com.schilings.neiko.security.oauth2.authorization.server.ApplicationEventAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2AccessTokenAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {

	public OAuth2AccessTokenAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		getDelegate().onAuthenticationSuccess(request, response, authentication);
		getApplicationEventPublisher().publishEvent(new OAuth2AccessTokenAuthenticationSuccessEvent(authentication));
	}

}
