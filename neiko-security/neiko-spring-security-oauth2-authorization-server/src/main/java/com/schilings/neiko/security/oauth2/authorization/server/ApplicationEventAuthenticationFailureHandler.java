package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public abstract class ApplicationEventAuthenticationFailureHandler
		implements AuthenticationFailureHandler, ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	private final AuthenticationFailureHandler delegate;

	protected ApplicationEventAuthenticationFailureHandler(AuthenticationFailureHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public AuthenticationFailureHandler getDelegate() {
		return delegate;
	}

}
