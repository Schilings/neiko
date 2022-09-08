package com.schilings.neiko.extend.sa.token.holder;

import com.schilings.neiko.common.util.spring.SpringUtils;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AbstractAuthenticationFailureEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AuthenticationSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.LogoutSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.AuthorityInitEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.AuthenticationImpl;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.ClientDetails;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
public class ApplicationEventPublisherHolder {

	public static void publishEvent(ApplicationEvent event) {
		SpringUtils.publishEvent(event);
	}

	public static void publishAuthenticationSuccessEvent(Map token) {
		AuthenticationImpl authentication = new AuthenticationImpl();
		authentication.setToken(token);
		authentication.setUserDetails(RBACAuthorityHolder.getUserDetails());
		authentication.setAuthenticated(true);
		publishEvent(new AuthenticationSuccessEvent(authentication));
	}

	public static void publishLogoutSuccessEvent() {
		AuthenticationImpl authentication = new AuthenticationImpl();
		authentication.setUserDetails(RBACAuthorityHolder.getUserDetails());
		authentication.setAuthenticated(true);
		publishEvent(new LogoutSuccessEvent(authentication));
	}

	public static void pushAbstractAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
		publishEvent(event);
	}

	public static void publishRoleAuthorityChangedEvent(Collection<String> userIds) {
		publishEvent(new RoleAuthorityChangedEvent(userIds));
	}

	public static void publisbAuthorityInitEvent(Object loginId,String loginType,String tokenValue) {
		publishEvent( new AuthorityInitEvent(loginId,loginType,tokenValue));
	}
}
