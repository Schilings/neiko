package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public abstract class ApplicationEventAuthenticationSuccessHandler
		implements AuthenticationSuccessHandler, ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	private final AuthenticationSuccessHandler delegate;

	public ApplicationEventAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public AuthenticationSuccessHandler getDelegate() {
		return delegate;
	}

}
