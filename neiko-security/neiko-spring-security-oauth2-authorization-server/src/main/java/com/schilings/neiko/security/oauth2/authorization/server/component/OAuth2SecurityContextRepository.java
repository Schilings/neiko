package com.schilings.neiko.security.oauth2.authorization.server.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.security.Principal;

/**
 * OAuth2 下使用的 SecurityContextRepository，从 bearerTokenResolver 中解析对应的 access_token, 然后利用
 * oAuth2AuthorizationService 来获取对应的 SecurityContext
 */
public class OAuth2SecurityContextRepository implements SecurityContextRepository {

	private final BearerTokenResolver bearerTokenResolver;

	private final OAuth2AuthorizationService authorizationService;

	public OAuth2SecurityContextRepository(OAuth2AuthorizationService authorizationService) {
		this.bearerTokenResolver = new CookieBearerTokenResolver();
		this.authorizationService = authorizationService;
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		String bearerToken = this.bearerTokenResolver.resolve(request);
		if (!StringUtils.hasText(bearerToken)) {
			return SecurityContextHolder.createEmptyContext();
		}
		OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(bearerToken,
				OAuth2TokenType.ACCESS_TOKEN);
		if (oAuth2Authorization == null) {
			return SecurityContextHolder.createEmptyContext();
		}
		Authentication authentication = (Authentication) oAuth2Authorization.getAttributes()
				.get(Principal.class.getName());
		return new SecurityContextImpl(authentication);
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		//
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return this.loadDeferredContext(request) != null;
	}

}
