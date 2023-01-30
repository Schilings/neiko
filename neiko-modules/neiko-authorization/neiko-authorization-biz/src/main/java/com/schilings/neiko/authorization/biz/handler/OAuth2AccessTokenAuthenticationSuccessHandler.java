package com.schilings.neiko.authorization.biz.handler;

import com.schilings.neiko.authorization.common.event.OAuth2AccessTokenAuthenticationSuccessEvent;
import com.schilings.neiko.security.oauth2.authorization.server.handler.ApplicationEventAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OAuth2AccessTokenAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {

	public OAuth2AccessTokenAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		getDelegate().onAuthenticationSuccess(request, response, authentication);
		publishEvent(request, response, authentication);
	}

	public void publishEvent(HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
		OAuth2AccessTokenAuthenticationSuccessEvent event = null;
		if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
			OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
			Map<String, Object> attributes = new HashMap<>(accessTokenAuthentication.getAdditionalParameters());
			attributes.put(OAuth2ParameterNames.CLIENT_ID, accessTokenAuthentication.getRegisteredClient().getClientId());
			attributes.put(OAuth2ParameterNames.SCOPE, accessTokenAuthentication.getAccessToken().getScopes());
			//attributes.put(OAuth2ParameterNames.GRANT_TYPE, "");
			event = new OAuth2AccessTokenAuthenticationSuccessEvent(request, response, authentication,attributes);
		} else {
			event = new OAuth2AccessTokenAuthenticationSuccessEvent(request, response, authentication);
		}
		getApplicationEventPublisher().publishEvent(event);
	}

}
