package com.schilings.neiko.authorization.common.event;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;
import java.util.Set;

/**
 *
 * <p>
 * AccessToken销毁成功事件，即注销成功事件
 * </p>
 *
 * @author Schilings
 */
public class OAuth2TokenRevocationAuthenticationSuccessEvent extends OAuth2AuthenticationEvent {

	public OAuth2TokenRevocationAuthenticationSuccessEvent(Authentication authentication) {
		super(authentication);
	}

	public OAuth2TokenRevocationAuthenticationSuccessEvent(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		super(request, response, authentication);
	}

	public OAuth2TokenRevocationAuthenticationSuccessEvent(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, Map<String, Object> attributes) {
		super(request, response, authentication, attributes);
	}

	public String getToken() {
		return getAttribute(OAuth2ParameterNames.TOKEN);
	}

}
