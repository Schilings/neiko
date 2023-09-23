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
 * AccessToken颁布成功事件，即成功登录事件
 * </p>
 *
 * @author Schilings
 */
public class OAuth2AccessTokenAuthenticationSuccessEvent extends OAuth2AuthenticationEvent {

	public OAuth2AccessTokenAuthenticationSuccessEvent(Authentication authentication) {
		super(authentication);
	}

	public OAuth2AccessTokenAuthenticationSuccessEvent(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		super(request, response, authentication);
	}

	public OAuth2AccessTokenAuthenticationSuccessEvent(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, Map<String, Object> attributes) {
		super(request, response, authentication, attributes);
	}

	public String getClientId() {
		return getAttribute(OAuth2ParameterNames.CLIENT_ID);
	}

	public Set<String> getScope() {
		return getAttribute(OAuth2ParameterNames.SCOPE);
	}

}
