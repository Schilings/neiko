package com.schilings.neiko.samples.application.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Component
public class TokenHolder {

	public String getTokenWithPrefix() {
		return BEARER.getValue() + " " + getToken();
	}

	public String getToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?> authenticationToken) {
			return authenticationToken.getToken().getTokenValue();
		}
		return null;
	}

}
