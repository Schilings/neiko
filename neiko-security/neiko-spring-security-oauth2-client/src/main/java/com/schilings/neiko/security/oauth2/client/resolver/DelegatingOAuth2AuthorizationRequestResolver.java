package com.schilings.neiko.security.oauth2.client.resolver;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class DelegatingOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver defaultResolver;

	private List<OAuth2AuthorizationRequestResolver> resolvers;

	private OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer;

	public DelegatingOAuth2AuthorizationRequestResolver(List<OAuth2AuthorizationRequestResolver> resolvers,
			OAuth2AuthorizationRequestResolver defaultResolver) {
		this.defaultResolver = defaultResolver;
		Assert.notNull(resolvers, "resolverMap can not be null.");
		this.resolvers = Collections.unmodifiableList(new ArrayList<>(resolvers));
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = null;
		if (!CollectionUtils.isEmpty(this.resolvers)) {
			for (OAuth2AuthorizationRequestResolver resolver : this.resolvers) {
				authorizationRequest = resolver.resolve(request);
				if (authorizationRequest != null) {
					break;
				}
			}
		}
		if (authorizationRequest == null) {
			authorizationRequest = this.defaultResolver.resolve(request);
		}
		if (authorizationRequest != null && this.authorizationRequestCustomizer != null) {
			this.authorizationRequestCustomizer.customize(request, authorizationRequest);
			return authorizationRequest;
		}
		return authorizationRequest;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
		if (registrationId == null) {
			return null;
		}
		OAuth2AuthorizationRequest authorizationRequest = null;
		if (!CollectionUtils.isEmpty(this.resolvers)) {
			for (OAuth2AuthorizationRequestResolver resolver : this.resolvers) {
				authorizationRequest = resolver.resolve(request, registrationId);
				if (authorizationRequest != null) {
					break;
				}
			}
		}
		if (authorizationRequest == null) {
			authorizationRequest = defaultResolver.resolve(request, registrationId);
		}
		if (authorizationRequest != null && this.authorizationRequestCustomizer != null) {
			this.authorizationRequestCustomizer.customize(request, authorizationRequest);
			return authorizationRequest;
		}
		return authorizationRequest;
	}

	public void setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer) {
		this.authorizationRequestCustomizer = authorizationRequestCustomizer;
	}

}
