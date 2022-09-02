package com.schilings.neiko.extend.sa.token.holder;

import com.schilings.neiko.common.util.spring.SpringUtils;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AbstractAuthenticationFailureEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AuthenticationSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.AuthenticationImpl;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.ClientDetails;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import java.util.Map;

public class ApplicationEventPublisherHolder {

	private static ApplicationEventPublisher eventPublisher = null;

	static {
		eventPublisher = SpringUtils.getApplicationContext();
		Assert.notNull(eventPublisher,
				"ApplicationEventPublisher in ApplicationEventPublisherHolder must not be null.");
	}

	public static void publishEvent(ApplicationEvent event) {
		eventPublisher.publishEvent(event);
	}

	public static void publishEvent(Object event) {
		eventPublisher.publishEvent(event);
	}

	public static void pushAuthenticationSuccessEvent(Map token) {
		AuthenticationImpl authentication = new AuthenticationImpl();
		authentication.setToken(token);
		authentication.setUserDetails(RBACAuthorityHolder.getUserDetails());
		authentication.setAuthenticated(true);
		publishEvent(new AuthenticationSuccessEvent(authentication));
	}

	public static void pushLogoutSuccessEvent() {
		AuthenticationImpl authentication = new AuthenticationImpl();
		authentication.setUserDetails(RBACAuthorityHolder.getUserDetails());
		authentication.setAuthenticated(true);
		publishEvent(new AuthenticationSuccessEvent(authentication));
	}

	public static void pushAbstractAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
		publishEvent(event);
	}

}
