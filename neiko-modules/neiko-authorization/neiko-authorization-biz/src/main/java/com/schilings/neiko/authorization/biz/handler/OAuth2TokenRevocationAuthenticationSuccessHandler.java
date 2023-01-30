package com.schilings.neiko.authorization.biz.handler;

import com.schilings.neiko.authorization.common.event.OAuth2TokenRevocationAuthenticationSuccessEvent;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.security.oauth2.authorization.server.handler.ApplicationEventAuthenticationSuccessHandler;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class OAuth2TokenRevocationAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {

	public OAuth2TokenRevocationAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		getDelegate().onAuthenticationSuccess(request, response, authentication);
		publishEvent(request, response, authentication);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(JsonUtils.toJson(R.ok()));
		response.getWriter().flush();
	}


	public void publishEvent(HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
		OAuth2TokenRevocationAuthenticationSuccessEvent event = null;
		if (authentication instanceof OAuth2TokenRevocationAuthenticationToken) {
			OAuth2TokenRevocationAuthenticationToken tokenRevocationAuthentication = (OAuth2TokenRevocationAuthenticationToken) authentication;
			Map<String, Object> attributes = new HashMap<>();
			attributes.put(OAuth2ParameterNames.TOKEN, tokenRevocationAuthentication.getToken());
			//attributes.put(OAuth2ParameterNames.GRANT_TYPE, "");
			event = new OAuth2TokenRevocationAuthenticationSuccessEvent(request, response, authentication,attributes);
		} else {
			event = new OAuth2TokenRevocationAuthenticationSuccessEvent(request, response, authentication);
		}
		getApplicationEventPublisher().publishEvent(event);
	}

}
